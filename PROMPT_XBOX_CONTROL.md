# Prompt de desenvolvimento — Xbox Control

Crie um aplicativo Android nativo chamado **Xbox Control**, em Kotlin, para Android 8.0 ou superior, com interface em português e foco em uso em modo paisagem.

## Objetivo
Exibir um controle virtual inspirado no layout de um controle Xbox sobre outros aplicativos, especialmente Roblox. O controle deve ter aparência horizontal, botões A, B, X, Y, LB, RB, LT, RT, Start, Menu, direcional (D-pad) e analógicos esquerdo e direito.

## Requisitos funcionais
1. Tela inicial com opções para conceder permissão de sobreposição, ativar o serviço de acessibilidade e iniciar/parar o controle.
2. Sobreposição usando `TYPE_APPLICATION_OVERLAY`, com áreas de toque limitadas aos controles; regiões transparentes fora dos botões devem continuar recebendo os toques normais do Roblox.
3. Usar um `AccessibilityService` com `canPerformGestures=true` e `dispatchGesture` para reproduzir toques e movimentos configurados pelo usuário. Explicar claramente que o Android exige ativação manual do serviço e que o funcionamento depende da versão do Android, do fabricante e do jogo.
4. Analógicos devem permitir pressionar, mover continuamente e soltar, sem deixar gestos presos. O D-pad deve suportar as oito direções.
5. Botões de ação devem permitir configurar alvos de toque na tela do jogo, com editor visual para arrastar e salvar as posições.
6. Permitir ajustar tamanho e transparência, reposicionar controles, restaurar o layout padrão e ocultar/mostrar a sobreposição.
7. O overlay deve ser encerrado por ação explícita do usuário e por uma notificação persistente enquanto o serviço estiver ativo.
8. Tratar permissões negadas, serviço de acessibilidade indisponível, rotação da tela, falhas de gesto e encerramento do serviço sem travar o app.
9. Respeitar privacidade: não coletar, transmitir ou armazenar dados do Roblox; configurações locais apenas.
10. Não afirmar que o app transforma o overlay em um controle Bluetooth/XInput real. É um mapeador de gestos de toque na tela, não um driver de controle físico.

## Qualidade e compatibilidade
- Projeto Gradle completo, compilável em JDK 17, com Android Gradle Plugin e Kotlin compatíveis.
- Código organizado em Activities, Services, modelos e Views.
- Declarar somente permissões necessárias no Manifest.
- Não usar APIs privadas, root ou métodos para contornar proteções do Android.
- Incluir README com instruções claras de instalação e ativação.
- Incluir testes básicos para mapeamento de botões e cálculos dos analógicos.
- Criar workflow do GitHub Actions que compile `assembleDebug` em push e permita baixar o APK como artefato chamado `Xbox-Control-debug`.
- O APK gerado deve se chamar `Xbox-Control.apk` e ser identificado como versão de teste, não como versão final validada.

## Critérios de aceitação
- O app instala e abre em Android compatível.
- A permissão de sobreposição é solicitada pelo fluxo oficial do Android.
- A ativação da acessibilidade é feita pelo usuário nas Configurações do Android.
- O usuário consegue mostrar e ocultar o controle.
- Toques fora dos controles continuam passando para o jogo.
- Os botões e analógicos enviam gestos configurados e podem ser reposicionados.
- O workflow termina com sucesso e publica o APK debug como artefato baixável.

Antes de declarar o projeto pronto, compile, corrija os erros e informe limitações reais. Não prometa compatibilidade universal com Roblox ou com todos os aparelhos.
