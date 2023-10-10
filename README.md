# Vamos Rachar?

> Aluno: João Victor Alves
> 
> 
> **Matrícula:** 509697
> 
> **Disciplina:** Programação para Dispositivos Móveis
> 
> **Professor:** Dr. Windson Viana
> 

## Atividade:

Você deve fazer um aplicativo que facilite a vida de quem vai dividir a conta no restaurante com os amigos. Ele pode ter apenas uma única tela conforme mostrado no final do vídeo da aula gravada- Parte 2.

As features principais são:

1. (2 pontos) - Por enquanto, o aplicativo só faz divisões do valor pelo número de pessoas
2. (2 pontos) - Mas ele já tem um ícone
3. (2 pontos) - Já permite o compartilhamento do valor final
4. (2 pontos) - Fala o valor calculado usando TTS
5. (2 pontos) - O usuário não precisa clicar para calcular, ele já faz automaticamente após o preenchimento dos campos de valor e número de pessoas

## Ícone, Paleta de Cores e Novo Layout:
| Ícone                                   | Paleta de Cores                                                        |
| --------------------------------------- | ---------------------------------------------------------------------- |
| <img src="https://github.com/joaoVictorBAlves/VamosRachar/assets/86852231/936867bf-6554-4fb4-9157-aec41f32e4ba" width="200"> |![Colorspaleta](https://github.com/joaoVictorBAlves/VamosRachar/assets/86852231/efb5af0c-f614-4a4d-a925-2548257d8a4b) |

### Novo layout e temas:
Com base nos dois tópicos anteriores e na tela já existente, os temas _claro_ e _escuro_ foram mudados e também ocorreu melhorias de hierarquia e ajuste nas margens dos componentes.
| Layout Claro                                                  | Layout Escuro                                                |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| <img src="https://github.com/joaoVictorBAlves/VamosRachar/assets/86852231/2147a89c-259d-40d7-818c-9fbeb91c3f8b" width="500"> | <img src="https://github.com/joaoVictorBAlves/VamosRachar/assets/86852231/5318f296-6893-4b3d-8389-ada77e8e71eb" width="500"> |

## Divisão dos valores

Para a realização da divisão foi usando uam classe interna que herda de `TextWatcher` para monitorar os campos de textos, com isso, conseguimos usar a função `onTextChange`. Nela capturamos os valores dos campos de texto e convertemos para Double, caso os valores estejam corretos eles fazem a divisão e atribui para o `result`:
```kotlin
 inner class TextChangeListener : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        @SuppressLint("SetTextI18n")
        override fun afterTextChanged(s: Editable?) {
            val price = edPrice.text.toString()
            val people = edPeople.text.toString()
            Log.d("TEXT", "$price and $people")

            if (price.isNotEmpty() && people.isNotEmpty()) {
                val df = DecimalFormat("#.00")
                val priceInput = edPrice.text.toString()
                val peopleInput = edPeople.text.toString()

                val price = if (priceInput.isNotEmpty()) priceInput.toDouble() else 0.0
                val people = if (peopleInput.isNotEmpty()) peopleInput.toDouble() else 0.0

                if (price == 0.0 || people == 0.0) {
                    val resultValue = "Informe valores válidos!"
                    result.text = resultValue
                } else {
                    val resultValue = df.format(price/people)
                    result.text = "R$$resultValue"
                }
            }

        }
    }
```

## Compartilhamento do resultado final

Para o compartilhamento do valor foi usado o evento para o `setOnClickListener`, nele pegamos os valores, fazemos os testes se os campos estão vazios ou se a resposta está alertando valores inválidos, nestes caso não é possível compartilhar, porém, estando tudo correto, o programa chama a `Intent.ACTION_SEND` para enviar a mensagem no formato "Vamos rachar: sua parte na conta é ${result.text}!":
```kotlin
       /**
         * Start Intents by button events
         */
        btnSend.setOnClickListener(View.OnClickListener {
            val price = edPrice.text.toString()
            val people = edPeople.text.toString()

            if (price.isNotEmpty() && people.isNotEmpty()) {
                if(result.text != "Informe valores válidos!" && result.text != ""){
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Vamos rachar: sua parte na conta é ${result.text}!")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                }
            }
        })
```

## Falar o valor do resultado

Para usar o TTS usamos o evento de clique do botão, e testamos se os campos foram preenchidos ou se a respostas está alertando valores inválidos e chamamos a função `speakResult`:

```kotlin
 btnVoice.setOnClickListener(View.OnClickListener {
    val price = edPrice.text.toString()
    val people = edPeople.text.toString()

    if (price.isNotEmpty() && people.isNotEmpty()) {
        if(result.text != "Informe valores válidos!" && result.text != "") {
            speakResult("A conta deu ${result.text} pra cada um!")
        }
    }
})
```

Usamos então o objeto textToSpeech e passamos o texto para que seja falado
```kotlin
   /**
     * Speak the result
     */
    private fun speakResult(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }
```
Usamos as funções onInit para configurar o TTS definindo a linguagem local do sistema. Também usamos o onDestroy para parar o áudio:

```kotlin
  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
        val locale = Locale.getDefault()
        val result = textToSpeech.setLanguage(locale)

        if (result == TextToSpeech.LANG_MISSING_DATA ||
            result == TextToSpeech.LANG_NOT_SUPPORTED
        ) {

        }
    }
}

override fun onDestroy() {
    super.onDestroy()
    textToSpeech.stop()
    textToSpeech.shutdown()
}
```

## Prompts usados
_Para a realização da tarefa foi necessário algumas vezes utilizar o ChatGPT, principalmente para corrigir erros e refatorar o código. Segue o link dos [Prompts utilizados](https://chat.openai.com/share/f2fec0be-9976-4db0-9345-02e50f74f11b)._

## Resultado final
| Layout Claro                                                  | Layout Escuro                                                |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
|![ezgif com-video-to-gif (3)](https://github.com/joaoVictorBAlves/VamosRachar/assets/86852231/2d12c362-15a2-4981-ab52-8c97775339d3) | ![ezgif com-video-to-gif (2)](https://github.com/joaoVictorBAlves/VamosRachar/assets/86852231/b8a4ce0e-7c83-414e-8a2b-8f39623a28a3) |
