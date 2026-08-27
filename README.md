# 📱 Protótipo Mobile - Georreferenciamento de Placas

Aplicativo Android nativo desenvolvido para validar o fluxo de captura e envio de placas. O app utiliza a câmera do dispositivo para registrar uma imagem, extrai as coordenadas de GPS (latitude e longitude) no momento do clique, e envia tudo em formato `multipart/form-data` para a API.

## 🛠️ Tecnologias Utilizadas
* **Linguagem:** Kotlin
* **Interface (UI):** Jetpack Compose (MVVM)
* **Requisições HTTP:** Retrofit2 + OkHttp3
* **Câmera:** Câmera nativa via `FileProvider`
* **Geolocalização:** FusedLocationProviderClient (Google Play Services)

---

## 🚀 Como Rodar o App Localmente

Para testar o aplicativo em conjunto com o backend rodando na sua máquina, siga os passos abaixo:

### 1. Pré-requisitos
* **Android Studio** instalado.
* Um celular físico Android (com Depuração USB/Wi-Fi ativada) ou um Emulador.
* **Importante:** Seu PC (rodando o backend) e o celular devem estar conectados na **mesma rede Wi-Fi**.

### 2. Configurando o IP do Backend
O app precisa saber o IP local da sua máquina para conseguir enviar os dados. 

1. Descubra o IP local da máquina onde o backend está rodando (No Linux/Mac: `ip a` ou `ifconfig`, procure pela interface Wi-Fi, ex: `192.168.100.X`).
2. No Android Studio, abra o arquivo:
   `app/src/main/java/com/seuprojeto/placas/data/api/RetrofitClient.kt`
3. Altere a variável `BASE_URL` para o IP da sua máquina com a porta do FastAPI:
   ```kotlin
   // Exemplo: Substitua pelo seu IP
   private const val BASE_URL = "[http://192.168.100.22:8000/](http://192.168.100.22:8000/)"
