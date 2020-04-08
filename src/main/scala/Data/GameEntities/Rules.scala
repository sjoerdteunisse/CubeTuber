package Data.GameEntities

import Data.Paradigm.Paradigm
import Data.GameEntities.Result.Result

object Rules {

  def defaultRules(movePlayer1: Paradigm, movePlayer2: Paradigm, movePlayer3: Paradigm, movePlayer4: Paradigm): Result = {
    (movePlayer1, movePlayer2, movePlayer3, movePlayer4) match {
      case _ => Result.PLAYER1_WON
    }
  }
}
