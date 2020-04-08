import javax.swing.{JFrame, JLabel, JPanel}
import java.awt.{Color, GridLayout}

import Data.GameEntities.{Rules, StaticAI, TurnResult}
import Data.Paradigm.Paradigm
import Data.Player.Player
import Data.{Paradigm, Player}
import GUI.GameGUI

import scala.annotation.tailrec
import scala.util.Random

object Run {
  /**
   * Example of how to use the GUI
   */

  def createPlayer(gameGUI: GameGUI, name:String, paradigm: Paradigm, player: Player) : List[TurnResult] => Paradigm ={
    gameGUI.setPlayerName(player, name)
    gameGUI.setParadigmProbability(player, paradigm, 5)
    uPlayer.createPlayer(player, gameGUI);
  }

  @tailrec
  def drawClusters(gameGUI: GameGUI, iteration: Int = 0, elemX: Int = 0 , elemY: Int = 0) : Unit ={
    val sizeX, sizeY = gameGUI.gridSize;

    println("iteration: " + iteration + ", X: " + elemX + "Y: " + elemY)
    val sR = scala.util.Random

    val pink = Color.pink
    val yellow = Color.yellow
    val orange = Color.orange

    val currentSize = elemX * elemY;
    val boardSize = sizeX * sizeY;
    val sizeLeft = boardSize - currentSize;

    val strategy = sizeLeft / 1;

    println("sizeLeft = " + sizeLeft)
    println("boardSize = " + boardSize)
    println("currentSize = " + currentSize)

    @tailrec
    def drawNearestCluster(size:Int, sX:Int, sY:Int, color: Color) : Unit ={
      if(size > 0) { // check recursion bounds
        val r = scala.util.Random
        val direction = r.nextInt(2) // 1 - 0

        println("random : " + direction)

        (sX, sY) match{
          case (sx, sy) if sx <(sizeX) && sy < (sizeY) => gameGUI.changeSquare(sX, sY, color)
          case _ => println("out of bounds, draw complete")
        }

        (direction, sX, sY) match {
          case (vec, sX, sY) if vec == 0 && sX < (sizeX) && sY < (sizeY) => drawNearestCluster(size - 1, sX , sY , color)
          case (vec, sX, sY) if vec == 1 && sX < (sizeX) && sY < (sizeY) => drawNearestCluster(size - 1, sX, sY , color)
          case _ => println("clustering done")
        }
      }
    }

    def getColor() : Color ={
     sR.nextInt(3) match {
        case 0 => pink
        case 1 => orange
        case 2 => yellow
      }
    }

    (elemX, elemY) match{
      case (x,y) if x < sizeX  => drawNearestCluster(strategy , elemX, elemY, getColor())
      case (x,y) if y < sizeY => drawNearestCluster(strategy, 0, elemY, getColor())
      case (x,y) if x == sizeX && y == sizeY => println("Done drawing")
    }

    println("sizeLeft + " + sizeLeft)
    sizeLeft match {
      case x if x > 0 && elemX < sizeX => drawClusters(gameGUI: GameGUI, iteration + 1 , elemX + 1, elemY)
      case x if x > 0 && elemY < sizeY && elemY < sizeY => drawClusters(gameGUI: GameGUI, iteration + 1 , 0, elemY + 1)
      case _ => println("no tiles left to draw, initialization of paradigm tiles done")
    }
  }

  def drawPlayers(gameGUI: GameGUI) : Unit ={
    val gridMax = gameGUI.gridSize - 1;

    gameGUI.changeSquare(0, 0, Color.BLUE)
    gameGUI.changeSquare(gridMax, gridMax, Color.GREEN)
    gameGUI.changeSquare(gridMax, 0, Color.RED)
    gameGUI.changeSquare(0, gridMax, Color.WHITE)
  }

  def main(args: Array[String]): Unit = {
    // Create the GUI once in your game. Specify how many squares the grid should have on each side.
    // Optionally you could specify a second parameter for the size of a single square. The default is 25.
    val gameGUI = GameGUI.createGameGUI(10, 25)

    val cluster = Array.ofDim[Paradigm](gameGUI.gridSize - 2, gameGUI.gridSize - 2)

    //Draw some clusters :)
    drawClusters(gameGUI)

    //Draw players
    drawPlayers(gameGUI)

    val staticAi = StaticAI.createStaticAI(Paradigm.FUNCTIONAL);
    val pl1 = createPlayer(gameGUI, "Game user 1", Paradigm.DECLARATIVE, Player.PLAYER1)
    val pl2 = createPlayer(gameGUI, "Game user 2", Paradigm.FUNCTIONAL, Player.PLAYER2)
    val pl3 = createPlayer(gameGUI, "Game user 3", Paradigm.FUNCTIONAL, Player.PLAYER3)
    val pl4 = createPlayer(gameGUI, "Game user 4", Paradigm.OO, Player.PLAYER4)

    val rules = Rules.defaultRules _
    val game = new Game(gameGUI, pl1, pl2, pl3, pl4, rules)
    gameLoop(game, gameGUI)
  }


  @tailrec
  def gameLoop(game: Game, gameGUI: GameGUI, depth:Int = 1): Unit = {

    val turnResult = game.playTurn()

    println("depth =" + depth)

    def redraw(posX:Int = 0, posY:Int = 0): Unit = {

      def transitionCubeTubers : Any = {
        val r = scala.util.Random
        val chance = r.nextInt(2) // 1 - 0

        RenderEachCornerForDepth(1, depth, r.nextInt(2))
        RenderEachCornerForDepth(2, depth, r.nextInt(2))
        RenderEachCornerForDepth(3, depth, r.nextInt(2))
        RenderEachCornerForDepth(4, depth, r.nextInt(2))

        def RenderEachCornerForDepth(playerStrategy: Int, depth: Int, chance:Int) = {
          println(posX,posY)
          depth match {
            case x if x > 0 =>{
              playerStrategy match {
                case 1 => {
                  (posX, posY) match {
                    case (x,y) if x == 0 && y == 0  => println("player 1", x, y)
                    case (x,y) if chance == 1 && x <= depth && y <= depth && x < gameGUI.gridSize && y < gameGUI.gridSize && gameGUI.getSquareColor(x, y) == Color.pink => gameGUI.changeSquare(x, y, Color.black)
                    case (x,y) if chance == 1 &&  x <= depth && y <= depth && x < gameGUI.gridSize && y < gameGUI.gridSize && gameGUI.getSquareColor(x, y) == Color.yellow => gameGUI.changeSquare(x, y, Color.black)
                    case (x,y) if chance == 1 && x <= depth && y <= depth && x < gameGUI.gridSize && y < gameGUI.gridSize && gameGUI.getSquareColor(x, y) == Color.orange => gameGUI.changeSquare(x, y, Color.black)
                    case _ => println("outisde ", posX,posY)
                  }
                }

                case 2 => {
                  (posX, posY ) match {
                    case (x,y) if x == 0 && y == gameGUI.gridSize  => println("player 2", x, y)
                    case (x,y) if chance == 1 && x >= gameGUI.gridSize - depth -1  && y <= depth  && x < gameGUI.gridSize && y < gameGUI.gridSize && gameGUI.getSquareColor(x, y) == Color.pink => gameGUI.changeSquare(x, y, Color.CYAN)
                    case (x,y) if chance == 1 && x >= gameGUI.gridSize - depth -1 && y <= depth && x < gameGUI.gridSize  && y < gameGUI.gridSize && gameGUI.getSquareColor(x, y) == Color.yellow => gameGUI.changeSquare(x, y, Color.CYAN)
                    case (x,y) if chance == 1 && x >= gameGUI.gridSize - depth -1 && y <= depth  && x < gameGUI.gridSize && y < gameGUI.gridSize && gameGUI.getSquareColor(x, y) == Color.orange => gameGUI.changeSquare(x, y, Color.CYAN)
                    case _ => println("outisde 2 ", posX,posY)
                  }
                }

                case 3 => {
                  (posX, posY ) match {
                    case (x,y) if  x == 0 && y == gameGUI.gridSize - depth  => println("player 3", x, y)
                    case (x,y) if chance == 1 && x <= depth && y >= gameGUI.gridSize - depth -1 && x < gameGUI.gridSize && y < gameGUI.gridSize && gameGUI.getSquareColor(x, y) == Color.pink => gameGUI.changeSquare(x, y, Color.WHITE)
                    case (x,y) if chance == 1 && x <= depth && y >= gameGUI.gridSize - depth -1 && x < gameGUI.gridSize && y < gameGUI.gridSize && gameGUI.getSquareColor(x, y) == Color.yellow => gameGUI.changeSquare(x, y, Color.WHITE)
                    case (x,y) if chance == 1 && x <= depth && y >= gameGUI.gridSize - depth -1 && x < gameGUI.gridSize && y < gameGUI.gridSize && gameGUI.getSquareColor(x, y) == Color.orange => gameGUI.changeSquare(x, y, Color.WHITE)
                    case _ => println("outisde 4 ", posX,posY)
                  }
                }

                case 4 => {
                  (posX, posY ) match {
                    case (_,y) if x == gameGUI.gridSize && y == gameGUI.gridSize  => println("player 4 ", x, y)
                    case (x,y) if chance == 1 && x >= gameGUI.gridSize - depth -1 && y >= gameGUI.gridSize - depth -1 && x < gameGUI.gridSize && y < gameGUI.gridSize && gameGUI.getSquareColor(x, y) == Color.pink => gameGUI.changeSquare(x, y, Color.red)
                    case (x,y) if chance == 1 && x >= gameGUI.gridSize - depth -1 && y >= gameGUI.gridSize - depth -1 && x < gameGUI.gridSize && y < gameGUI.gridSize && gameGUI.getSquareColor(x, y) == Color.yellow => gameGUI.changeSquare(x, y, Color.red)
                    case (x,y) if chance == 1 && x >= gameGUI.gridSize - depth -1 && y >= gameGUI.gridSize - depth -1&& x < gameGUI.gridSize && y < gameGUI.gridSize && gameGUI.getSquareColor(x, y) == Color.orange => gameGUI.changeSquare(x, y, Color.red)
                    case _ => println("outisde 3 ", posX,posY)
                  }
                }
                case _ => println("corner render done..")
              }
            }
            case _ => println("Depth scan doesn't match, done..")
          }
        }
      }

      transitionCubeTubers

      (posX, posY) match{
        case (posX, posY) if posX < gameGUI.gridSize => redraw(posX + 1, posY)
        case (_, posY) if posY < gameGUI.gridSize => redraw(0, posY + 1)
        case _ => println("redraw complete")
      }
    }

    redraw()

    println("Player 1: " + turnResult.movePlayer1)
    println("Player 2: " + turnResult.movePlayer2)
    println("Result: " + turnResult.result)
    println("Score: " + game.getScorePlayer1() + "-" + game.getScorePlayer2())

    //gameLoop()
    //val AI = StaticAI.createStaticAI(Paradigm.FUNCTIONAL)



    gameLoop(game, gameGUI, depth + 1)
  }

}
