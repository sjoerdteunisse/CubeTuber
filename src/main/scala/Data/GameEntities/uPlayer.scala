import Data.GameEntities.TurnResult
import Data.Paradigm.Paradigm
import Data.Player.Player
import GUI.GameGUI

object uPlayer {
  def createPlayer(player: Player, gameGUI: GameGUI): (List[TurnResult]) => Paradigm  = {

    def getMove(turnHistory: List[TurnResult] ): Paradigm = {
      gameGUI.askInput(player) match {
        case value => value
      }
    }

    getMove
  }
}
