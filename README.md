Energy Tycoon
=============

Energy Tycoon é um jogo Idle / Tycoon desenvolvido em Java (Swing), onde o jogador produz energia (kWh) através de diferentes tipos de geradores, melhora sua eficiência e automatiza a produção contratando gerentes.

O projeto foi criado como prática de:
- Java Desktop (Swing)
- Programação Orientada a Objetos
- Timers e progressão incremental
- Salvamento e carregamento de estado do jogo

--------------------------------------------------

Gameplay
--------

- Comece com uma pequena quantidade de energia
- Compre geradores (limão, bicicleta, solar, eólico, usinas, etc.)
- Gere energia manualmente clicando no botão
- Faça upgrades para aumentar a produção
- Contrate gerentes para automatizar a geração
- O jogo salva automaticamente ao fechar

--------------------------------------------------

Funcionalidades
---------------

- Sistema Idle / Incremental
- Geradores com:
  - Custo inicial
  - Tempo de produção
  - Ganho por ciclo
  - Níveis e upgrades
- Gerentes para geração automática
- Interface gráfica em Java Swing
- Barra de progresso animada
- Salvamento e carregamento de jogo (savegame.txt)
- Botões e UI baseados em imagens

--------------------------------------------------

Tecnologias Utilizadas
---------------------

- Java 8+
- Swing (javax.swing)
- AWT
- javax.swing.Timer
- Programação Orientada a Objetos

--------------------------------------------------

Estrutura do Projeto
-------------------

br/com/harpyjam/
|
|-- TelaJogo.java        (Classe principal: UI + lógica do jogo)
|
|-- images/
|   |-- imgBtnGerar_0.png
|   |-- imgBtnGerar_1.png
|   |-- imgBtnComprar.png
|   |-- imgBtnComprarDes.png
|   |-- imgBtnMelhorar.png
|   |-- imgBtnMelhorarDes.png
|   |-- ...
|
|-- savegame.txt         (Criado automaticamente ao salvar)

--------------------------------------------------

Como Executar
------------

1) Clone o repositório
git clone https://github.com/GabryePatrickSoares/Jogo-Estilo-Idle-Game.git

2) Abra o projeto em uma IDE (IntelliJ, Eclipse ou NetBeans)

3) Execute a classe
TelaJogo.java

Obs:
A pasta images deve estar no classpath do projeto.

--------------------------------------------------

Sistema de Salvamento
--------------------

- O jogo salva automaticamente ao fechar a janela
- O progresso é armazenado em savegame.txt
- Dados salvos:
  - Energia atual
  - Geradores comprados
  - Níveis dos geradores
  - Gerentes ativos

--------------------------------------------------

Desenvolvido por: Gabryel P. Soares
Projeto para estudos e prototipagem de jogos Idle em Java.
