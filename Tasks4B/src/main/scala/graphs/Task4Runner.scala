package graphs

import java.io.File

object Task4Runner extends App {
  def printInfo(name: String, file: File, parser: Parsers.Parser): Unit = {
    println(s"Info for graph '$name':")
    val nodes = parser(file)
    println(s"Vertices: ${nodes.size}")
    val edges = nodes.iterator.map(_.neighbors.iterator.map(_._2).sum).sum / 2
    println(s"Edges: $edges")
    println(s"Triangles: ${Task4.findTriangles(nodes)}")
    println(s"Avg cluster coeff: ${Task4.averageClusterCoeff(nodes)}")
    println(s"Global cluster coeff: ${Task4.globalClusterCoeff(nodes)}")
  }

  val name = GraphNames.VK
  printInfo(name, graphs(name)._1, graphs(name)._2)
}
