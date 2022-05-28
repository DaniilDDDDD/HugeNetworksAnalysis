package graphs

import java.io.File

import scala.collection.immutable.TreeSet
import scala.collection.mutable

object Parsers {
  type Parser = File => TreeSet[Node]

  def parser(rule: String => (Int, Int))(file: File): TreeSet[Node] = {
    val src = io.Source.fromFile(file)
    val nodes = mutable.HashMap.empty[Int, Node]

    for(str <- src.getLines()) {
      val (id1, id2) = rule(str)
      val node1 = nodes.getOrElseUpdate(id1, new Node(id1))
      val node2 = nodes.getOrElseUpdate(id2, new Node(id2))
      node1 addEdge node2
    }

    src.close()
    TreeSet(nodes.values.toSeq: _*)
  }

  val google = parser(str => {
    val splitted = str.split("\t")
    (splitted(0).toInt, splitted(1).toInt)
  }) _

  val astro = google

  val vk = parser(str => {
    val splitted = str.split(",")
    (splitted(0).toInt, splitted(1).toInt)
  }) _
}
