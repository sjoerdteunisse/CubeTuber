import GUI.GameGUI
import Data.Paradigm.Paradigm
import Data.GameEntities.Result.Result
import Data.GameEntities.{Result, TurnResult}

class Game(gameGUI: GameGUI,
           getMovePlayer1: (List[TurnResult]) => Paradigm,
           getMovePlayer2: (List[TurnResult]) => Paradigm,
           getMovePlayer3: (List[TurnResult]) => Paradigm,
           getMovePlayer4: (List[TurnResult]) => Paradigm,
           getResult: (Paradigm, Paradigm, Paradigm, Paradigm) => Result) {

  private var scorePlayer1, scorePlayer2, scorePlayer3, scorePlayer4 = 0
  private var turnHistory: List[TurnResult] = List[TurnResult]();

  def getScorePlayer1(): Int = scorePlayer1
  def getScorePlayer2(): Int = scorePlayer2
  def getScorePlayer3(): Int = scorePlayer3
  def getScorePlayer4(): Int = scorePlayer4


  def playTurn(): TurnResult = {
    val movePlayer1 = getMovePlayer1(turnHistory)
    val movePlayer2 = getMovePlayer2(turnHistory)
    val movePlayer3 = getMovePlayer3(turnHistory)
    val movePlayer4 = getMovePlayer4(turnHistory)

    val result = getResult(movePlayer1, movePlayer2, movePlayer3, movePlayer4)

    result match {
      case Result.PLAYER1_WON => scorePlayer1 += 1
      case Result.PLAYER2_WON => scorePlayer2 += 1
      case Result.PLAYER3_WON => scorePlayer3 += 1
      case Result.PLAYER4_WON => scorePlayer4 += 1
    }

    val turnResult = new TurnResult(movePlayer1, movePlayer2, movePlayer3, movePlayer4, result)
    turnHistory =  turnResult :: turnHistory
    turnResult
  }
}

object Game {
  type Player = List[TurnResult] => Paradigm
}
