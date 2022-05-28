package graphs

import scala.collection.immutable.{HashMap, TreeSet}
import scala.collection.mutable

object TaskB {
  def findWeakComponents(graph: Graph): Components = {
    val allVerticesMap = graph.aliveNodes.map(n => (n.id, n)).toMap
    val verticesMap = mutable.HashMap.empty[Int, Node]
    allVerticesMap.foreach(x => verticesMap(x._1) = x._2)

    val componentsMap = mutable.HashMap.empty[Int, Int]

    def addNeighborsToStack(stack: List[(Int, Int)], node: Node): List[(Int, Int)] = {
      var st = stack
      for(headNb <- node.aliveNeighbors) st ::= (node.id, headNb._1.id)
      st
    }

    val firstNode = allVerticesMap.head._2
    val firstId = firstNode.id
    val initOpsStack = addNeighborsToStack(List.empty, firstNode)
    var curComp = 1
    componentsMap(firstId) = curComp
    verticesMap.remove(firstId)

    def go(opsStack: List[(Int, Int)]): Unit = opsStack match {
      case (from, to) :: opsLeft if !componentsMap.contains(to) =>
        componentsMap(to) = componentsMap(from)
        val toNode = verticesMap(to)
        verticesMap.remove(to)
        go(addNeighborsToStack(opsLeft, toNode))
      case (_, _) :: opsLeft => go(opsLeft)
      case Nil if verticesMap.nonEmpty =>
        val newComponent = curComp + 1
        curComp += 1
        val node = verticesMap.head._2
        componentsMap(node.id) = newComponent
        verticesMap.remove(node.id)
        go(addNeighborsToStack(List.empty, node))
      case Nil =>
    }

    go(initOpsStack)

    val res = mutable.HashMap.empty[Int, mutable.TreeSet[Node]]
    for((nodeId, compId) <- componentsMap) res.getOrElseUpdate(compId, mutable.TreeSet.empty[Node]) += allVerticesMap(nodeId)

    HashMap(res.map {
      case (i, nodes) => (i, TreeSet(nodes.toSeq: _*))
    }.toSeq: _*)
  }

  def maxWeakComponentPercent(graph: Graph): Double = {
    val components = findWeakComponents(graph)
    val maxComponent = components.maxBy(_._2.size)
    1.0 * maxComponent._2.size / graph.aliveNodesCount
  }
}
