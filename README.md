# E é isso — Controle Xbox Virtual para Roblox (Android / Kotlin)

Aplicativo Android nativo (Kotlin + XML) que desenha um **controle de Xbox virtual flutuante** por cima do jogo Roblox, usando janelas `TYPE_APPLICATION_OVERLAY` + `AccessibilityService` para executar **toques reais** na tela do jogo.

> ⚠️ Uso educacional / de acessibilidade. Use apenas em jogos onde você é o dono ou tem autorização para automatizar toques. Não utilize para trapear em jogos multiplayer.

---

## ✨ Recursos

- **Controle flutuante Xbox** desenhado por cima do Roblox.
- Cada botão é uma **janela `WindowManager` separada** com flags `FLAG_NOT_FOCUSABLE | FLAG_NOT_TOUCH_MODAL | FLAG_LAYOUT_IN_SCREEN`:
  - toque no botão → capturado pelo controle;
  - toque fora do botão → passa direto para o Roblox, sem travar o jogo.
- **Feedback visual + tátil**: botão muda de cor/brilho e vibra ao toque do dedo.
- **Multi-touch real**: segurar o Analógico Esquerdo enquanto toca no botão A funciona simultaneamente, porque cada botão é uma janela própria e o `AccessibilityService` despacha gestos independentes.
- **Analógicos contínuos**: gesto `StrokeDescription` com `willContinue = true` + `continueStroke` — o dedo virtual desliza na área de movimento do Roblox até `ACTION_UP`.
- **Botões de ação** (A, B, X, Y, LB, RB, LT, RT, Start, Select): `dispatchGesture` dispara um clique na coordenada do **Alvo de Toque** configurado.
- **D-Pad**: 8 direções com tap direcional no alvo.
- **Editor de layout** em paisagem: arraste botões e alvos, sliders de tamanho e transparência, salvar/resetar.
- Tela inicial escura estilo Xbox (preto/cinza + verde neon `#107C10`).

---

## 🧩 Estrutura do projeto

```
EeIssoApp/
├── settings.gradle
├── build.gradle                 (root: AGP 8.2.0, Kotlin 1.9.22)
├── gradle.properties
├── gradle/wrapper/gradle-wrapper.properties
└── app/
    ├── build.gradle             (minSdk 26, targetSdk 34, JDK 17)
    ├── proguard-rules.pro
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/eeisso/gamepad/
        │   ├── model/
        │   │   ├── ButtonType.kt          (enum dos controles)
        │   │   ├── ButtonConfig.kt        (dados persistidos por botão)
        │   │   └── LayoutManager.kt       (SharedPreferences: load/save/reset/defaults)
        │   ├── view/
        │   │   ├── GamepadInputListener.kt (interface de eventos)
        │   │   ├── FloatingButtonView.kt   (A/B/X/Y/LB/RB/LT/RT/Start/Select)
        │   │   ├── FloatingAnalogView.kt   (analógicos esquerdo/direito)
        │   │   └── FloatingDPadView.kt     (D-Pad 8 direções)
        │   ├── service/
        │   │   ├── OverlayService.kt              (janelas flutuantes + OnTouchListener)
        │   │   └── GamepadAccessibilityService.kt (gestos reais no Roblox / multi-touch)
        │   └── ui/
        │       ├── MainActivity.kt         (tela inicial estilo Xbox)
        │       └── LayoutEditorActivity.kt (editor drag-and-drop em paisagem)
        └── res/
            ├── drawable/    (ícone do controle, fundos dos botões, marcadores)
            ├── layout/      (activity_main.xml, activity_layout_editor.xml)
            ├── values/      (colors.xml, strings.xml, themes.xml)
            ├── xml/         (accessibility_service_config.xml, backup_rules, data_extraction_rules)
            └── mipmap-anydpi-v26/ (ic_launcher.xml adaptativo)
```

---

## 🔧 Como abrir e compilar no Android Studio

1. **Abra** o Android Studio → `File → Open` → selecione a pasta `EeIssoApp/`.
2. O Android Studio vai gerar automaticamente o `gradlew` e o `gradle-wrapper.jar` na primeira sincronização. (Se for compilar pela linha de comando e não tiver Gradle instalado, rode `gradle wrapper` uma vez para gerar o wrapper.)
3. Aguarde a sincronização do Gradle (baixa AGP 8.2.0, Kotlin 1.9.22, AndroidX, Material).
4. Conecte um dispositivo Android **real** (API 26+) com depuração USB ativada — **emulador não serve** para testar o overlay + acessibilidade de forma confiável.
5. `Run ▶` no app **E é isso**.

> Requer JDK 17 instalado no Android Studio (bundled na versão 2023.1+).

---

## ▶️ Como usar (passo a passo)

1. **Conceder Sobreposição**: toque em *Conceder Sobreposição* e autorize "Exibir sobre outros apps" para o app **E é isso**.
2. **Ativar Acessibilidade**: toque em *Ativar Acessibilidade* → role até **"E é isso - Toque Virtual"** → ative → confirme.
3. **Personalizar Layout** (recomendado na 1ª vez): toque em *PERSONALIZAR LAYOUT* e:
   - Arraste cada botão (A, B, X, Y, analógicos, D-Pad, ombros, gatilhos, Start, Select) para onde quiser.
   - Arraste o **Alvo de Toque** vermelho de cada botão de ação para cima do botão **real** correspondente dentro do Roblox (ex: Alvo A sobre o botão Pular, Alvo X sobre o botão Ataque).
   - Use os sliders para ajustar **Tamanho** e **Transparência** do botão selecionado.
   - Toque em **Salvar Layout**.
4. Abra o Roblox na partida desejada (idealmente em **paisagem/landscape**).
5. Volte ao app **E é isso** (ou toque na notificação persistente) e toque em **SOBREPOR CONTROLE**.
6. Os botões aparecem sobre o Roblox. Toque neles normalmente — o toque é reproduzido dentro do jogo.

> Os alvos de toque são **coordenadas absolutas da tela**. Se você girar o aparelho, abra o editor novamente na orientação em que vai jogar.

---

## 🧠 Como o toque funciona (detalhe técnico)

### Janela flutuante (OverlayService)

Cada botão é uma view adicionada individualmente ao `WindowManager`:

```kotlin
WindowManager.LayoutParams(
    size, size,
    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
    PixelFormat.TRANSLUCENT
)
```

`FLAG_NOT_TOUCH_MODAL` é a chave: o toque que cai **fora** do retângulo do botão atravessa até o Roblox; o toque que cai **dentro** é tratado pela `onTouchEvent` da view (que retorna `true`).

Como cada botão é uma janela própria, o sistema trata toques simultâneos em botões diferentes como **ponteiros independentes** — base do multi-touch real.

### Toque real no jogo (GamepadAccessibilityService)

O serviço de acessibilidade (com `canPerformGestures="true"`) despacha gestos via `dispatchGesture`:

- **Botão de ação**: `tapAt(targetX, targetY)` cria um `GestureDescription` com `StrokeDescription(path, 0, 35ms, willContinue=false)` e despacha. Cada tap é um gesto independente → combina com strokes em andamento como ponteiros extras.
- **Analógico**:
  - `ACTION_DOWN` → `startAnalog`: stroke inicial com `willContinue=true` em `(targetX, targetY)`.
  - `ACTION_MOVE` → `moveAnalog`: `continueStroke(path, 0, 60ms, willContinue=true)` estende o stroke até `(targetX + normX·range, targetY − normY·range)`. O Roblox vê o dedo deslizando.
  - `ACTION_UP`    → `stopAnalog`: `continueStroke(..., willContinue=false)` encerra o stroke.
  - Um *heartbeat* de 40 ms mantém o stroke vivo se o dedo parar sobre o analógico.

### Editor (LayoutEditorActivity)

Cada botão e cada alvo é uma view dentro de um `FrameLayout`. O `OnTouchListener` manual converte `ACTION_MOVE` (em `rawX/rawY`) em translação da view e atualiza `cfg.x/y` ou `cfg.targetX/Y`, persistidos no `SharedPreferences` ("eeisso_layout").

---

## 🗺️ Roadmap sugerido (não implementado)

- Editor para posicionar os alvos com captura de screenshot do Roblox ao fundo.
- Botão de "gravar macro" (sequência de taps).
- Mapeamento de botões a teclas do teclado físico via HID.

---

## 📜 Permissões declaradas

| Permissão | Uso |
| --- | --- |
| `SYSTEM_ALERT_WINDOW` | desenhar o controle flutuante |
| `BIND_ACCESSIBILITY_SERVICE` | executar gestos no Roblox |
| `VIBRATE` | feedback tátil ao tocar nos botões |
| `FOREGROUND_SERVICE` / `FOREGROUND_SERVICE_SPECIAL_USE` | manter o overlay vivo em segundo plano |
| `<queries> com.roblox.client` | detectar quando o Roblox está em foco |

---

## ⚖️ Licença

Código de exemplo entregue como está, para fins educacionais e de acessibilidade.
