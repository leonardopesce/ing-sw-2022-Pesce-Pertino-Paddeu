# Eriantys - Software Engineering Project (2021-2022)
<a href="https://www.craniocreations.it/prodotto/eriantys/">
    <img src="https://www.craniocreations.it/wp-content/uploads/2021/06/Eriantys_slider.jpg">
</a>

The <b>Software Engineering Final Exam</b> of the 2021-2022 school year requires the development of a software version (in Java) of the Eriantys board game, a Cranio Creations product that is inspired by and attempts to renew the already established Carolus Magnus.

Eriantys is a game full of strategy, tactics and twists. Plan your moves carefully and try to control the moves of your opponents. With three different game modes, including a team game, Eriantys offers always different and interesting games. Also, if you play with the expert variant, you can use the fantastic abilities of the special characters: each adds many possibilities, enriching the fun and beauty of the challenge.


<table align="center">
<tr><td>

| Name             |    ID    |
| :--------------- | :------: |
| Leonardo Pesce   | 10659489 |
| Paolo Pertino    | 10729600 | 
| Alberto Paddeu   | 10729194 | 

</td><td>

| Professor & Tutors | Role |
|:-------------------|:----:|
| Gianpaolo Cugola   | Software Engineering Professor |
| Davide Mazza       | Teaching Assistant             |
| Francesco Sammarco | Teaching Assistant             |
| Franco Chierici    | Teaching Assistant             |
| Alberto Archetti   | Teaching Assistant             |

</td></tr> </table>

> **_FINAL SCORE_** 30L / 30

## Implemented Feature Status
In the following table all the requested features with their current status are displayed.
| Feature name    | Description | Status |
|:----------------|:------------|:------:|
| Complete Rules  | The game must be played by 2 and 3 people and the expert mode with at least 8 character card must be included. | ✅ |
|       CLI       | The game has to be playable by using a text interface. | ✅ |
|       GUI       | The game has to be playable by using a graphical interface built with Swing or JavaFX. | ✅ |
|    Socket       | The clients and server need to be connected via TCP-IP sockets. | ✅ |
| 👑 Character Cards | The game has to be delivered with all the 12 character cards. | ✅ |
| 👑 4 players match | The game has to be delivered so that is possible to play games with 2,3 or 4 players. | ✅ |
| 👑 Multiple Games  | The server must handle multiple games simultaneously. | ✅ |
| 👑 Persistence     | The state of a match must be saved to disk in order for the game to be able to resume even after the server has stopped running. | ❌ |
| 👑 Disconnection resilience | Disconnected players can log back in later and continue the match. While a player is not logged in, the game continues by skipping that player's turns. | ❌ |

> **_KEY:_** [✅]() Implemented [❌]() Not Implemented [👑]() Advanced functionality

## Material Delivered
By clicking the icons below you'll be redirected to the delivered material.

| Installation | Run Eriantys | Javadocs | Weekly report | UML |
|:-------------|:-------------|:---------|:--------------|:----|
| <a target="_self" href="https://github.com/leonardopesce/ing-sw-2022-Pesce-Pertino-Paddeu/wiki/Installation">![][installation-logo]</a> | <a target="_self" href="https://github.com/leonardopesce/ing-sw-2022-Pesce-Pertino-Paddeu/wiki/Running">![][running-logo]</a> | <a target="_self" href="https://github.com/leonardopesce/ing-sw-2022-Pesce-Pertino-Paddeu/tree/main/Deliverables/Javadocs">![][javadocs-logo]</a> | <a target="_self" href="https://github.com/leonardopesce/ing-sw-2022-Pesce-Pertino-Paddeu/blob/main/Deliverables/eriantys_report.pdf">![][report-logo]</a> | <a target="_self" href="https://github.com/leonardopesce/ing-sw-2022-Pesce-Pertino-Paddeu/blob/main/Deliverables/UML">![][uml-logo]</a> |
| Installation steps | Running the jars | Project javadocs | Our weekly report | UML week by week

## Tests
Here are reported the coverage of our tests both for the model and controller.

![][tests-img]


[installation-logo]: markdown-assets/installation.gif
[running-logo]: markdown-assets/running.gif
[javadocs-logo]: markdown-assets/javadoc.gif
[report-logo]: markdown-assets/report.gif
[uml-logo]: markdown-assets/uml.gif
[tests-img]: markdown-assets/tests.png
