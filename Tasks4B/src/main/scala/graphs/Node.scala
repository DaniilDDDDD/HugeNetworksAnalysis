package graphs

import scala.collection.immutable.TreeMap
import scala.collection.mutable

class Node(val id: Int) {
  val neighbors = mutable.TreeMap.empty[Node, Int]
  def neighborsCount: Int = neighbors.size

  def deg: Int = neighbors.foldLeft(0)({
    case (d, (_, x)) => d + x
  })

  def degAlive: Int = neighbors.foldLeft(0)({
    case (d, (a, _)) if a.isKilled => d
    case (d, (_, x)) => d + x
  })

  def addEdge(x: Node): Unit = {
    neighbors(x) = neighbors.getOrElse(x, 0) + 1
    x.neighbors(this) = x.neighbors.getOrElse(this, 0) + 1
  }

  def isNeighborOf(x: Node): Option[Int] =
    (this.neighbors.get(x), x.neighbors.get(this)) match {
      case (Some(x), Some(y)) if x == y => Some(x)
      case (Some(_), _) => throw new Exception("Incorrect parsing")
      case (_, Some(_)) => throw new Exception("Incorrect parsing")
      case _ => None
    }

  override def equals(obj: Any): Boolean = obj match {
    case x: Node => x.id == this.id
    case _ => false
  }

  override def hashCode(): Int = id

  var isKilled: Boolean = false
  def aliveNeighbors: TreeMap[Node, Int] = TreeMap(neighbors.filter(!_._1.isKilled).toSeq: _*)
}

object Node {
  implicit val ordering: Ordering[Node] = Ordering.by(_.id)
}
