package Data.GameEntities

import Data.GameEntities.Result.Result
import Data.Paradigm.Paradigm

class TurnResult(val movePlayer1: Paradigm, val movePlayer2: Paradigm, val movePlayer3: Paradigm, val movePlayer4: Paradigm, val result: Result) {
  //new TurnResult(movePlayer2, movePlayer1, movePlayer3, movePlayer4, result)
}
