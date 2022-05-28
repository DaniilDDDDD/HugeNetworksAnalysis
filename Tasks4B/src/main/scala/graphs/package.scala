import java.io.File

import scala.collection.immutable.{HashMap, TreeSet}
import scala.util.Random

package object graphs {
  type Graph = TreeSet[Node]
  type Component = TreeSet[Node]
  type Components = HashMap[Int, Component]

  val graphs = Map(
    "web-Google" -> (new File("D:/System/Downloads/web-Google.txt"), Parsers.google),
    "CA-AstroPh" -> (new File("D:/System/Downloads/CA-AstroPh.txt"), Parsers.astro),
    "VK" -> (new File("D:/System/Downloads/vk.csv"), Parsers.vk)
  )

  object GraphNames {
    val Google = "web-Google"
    val AstroPh = "CA-AstroPh"
    val VK = "VK"
  }

  def parseGraph(name: String): Graph = graphs(name) match {
    case (file, parser) => parser(file)
  }

  def time(op: =>Unit): Long = {
    val start = System.nanoTime()
    op
    System.nanoTime() - start
  }

  def timeMany(times: Int)(op: =>Unit): Long = (0 until times).map(_ => time(op)).sum / times

  private val rand = new Random()
  def generateInt(from: Int, until: Int): Int =
    rand.nextInt(until - from) + from

  implicit class GraphOps(graph: Graph) {
    def killRandom(x: Int): Unit = {
      val nodesSeq = graph.aliveNodes.toVector
      val nodesCount = graph.aliveNodesCount
      var toRemove = nodesCount * x / 100
      while(toRemove > 0) {
        val killIdx = rand.nextInt(nodesCount)
        val killNode = nodesSeq(killIdx)
        if(!killNode.isKilled) {
          killNode.isKilled = true
          toRemove -= 1
        }
      }
    }

    def killMostDeg(x: Int): Unit = {
      val ordering = Ordering.by((n: Node) => n.degAlive).reverse

      val nodesByDeg = graph.aliveNodes.toVector.sorted(ordering)
      val nodesCount = graph.aliveNodesCount
      var toRemove = nodesCount * x / 100

      val iterator = nodesByDeg.iterator
      while(toRemove > 0 && iterator.hasNext) {
        val killNode = iterator.next()
        if(!killNode.isKilled) {
          killNode.isKilled = true
          toRemove -= 1
        }
      }
    }

    def makeVerticesAlive: Unit = { graph.foreach(_.isKilled = false) }
    def aliveNodes: TreeSet[Node] = { TreeSet(graph.filter(!_.isKilled).toSeq: _*) }

    def nodesCount: Int = graph.size
    def aliveNodesCount: Int = graph.count(!_.isKilled)
  }
}
