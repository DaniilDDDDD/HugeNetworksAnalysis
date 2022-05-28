package graphs

import scala.collection.immutable.TreeSet
import Implicits._

object Task4 {
  def verticesCount(nodes: TreeSet[Node]): Int = nodes.size
  def edgesCount(nodes: TreeSet[Node]): Int =
    nodes.iterator.map(_.neighbors.iterator.map(_._2).sum).sum / 2

  def findTriangles(vertices: TreeSet[Node]): Int = {
    var sum = 0
    for {
      v1 <- vertices
      (n1, _) <- v1.neighbors.iteratorFrom(v1).dropWhile(_._1 == v1)
      (n2, _) <- v1.neighbors.iteratorFrom(n1).dropWhile(_._1 == n1)
    } {
      n1 isNeighborOf n2 match {
        case Some(_) =>
          sum += 1
        case _ =>
      }
    }
    sum
  }

  def localClusterCoeff(node: Node): Double = {
    if(node.deg < 2) 0
    else {
      var e = 0
      for {
        (n1, _) <- node.neighbors if n1.id != node.id
        (n2, _) <- node.neighbors.iteratorFrom(n1).dropWhile(_._1 == n1)
      } {
        val edgesCount = (n1 isNeighborOf n2).getOrElse(0)
        e += edgesCount
      }
      val deg = node.deg
      2.0 * e / (deg - 1) / deg
    }
  }

  private def combOf2(n: Int): Int = n * (n - 1) / 2

  def globalClusterCoeff(nodes: Set[Node]): Double = {
    val sums = nodes.foldLeft((0.0, 0.0)) {
      case ((s1, s2), n) =>
        val comb = combOf2(n.neighborsCount)
        (s1 + comb * localClusterCoeff(n), s2 + comb)
    }
    sums._1 / sums._2
  }

  def averageClusterCoeff(nodes: Set[Node]): Double =
    nodes.sumBy(localClusterCoeff) / nodes.size
}
