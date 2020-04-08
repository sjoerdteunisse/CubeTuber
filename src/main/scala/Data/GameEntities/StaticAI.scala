package Data.GameEntities

import Data.Paradigm.Paradigm

object StaticAI {
  def createStaticAI(staticMove: Paradigm): (List[TurnResult]) => Paradigm  = {
    def getMove(turnHistory: List[TurnResult]): Paradigm = {
      staticMove
    }
    getMove
  }
}
