package object kmedianas {
  import scala.annotation.tailrec
  import scala.collection.{Map, Seq, mutable}
  import scala.collection.parallel.CollectionConverters._
  import scala.collection.parallel.{ParMap, ParSeq}
  import scala.util.Random

  class Punto(val x: Double, val y: Double, val z: Double) {
    private def cuadrado(v: Double): Double = v * v
    def distanciaAlCuadrado(that: Punto): Double =
      cuadrado(that.x - x) + cuadrado(that.y - y) + cuadrado(that.z - z)
    private def round(v: Double): Double = (v * 100).toInt / 100.0
    override def toString = s"(${round(x)}, ${round(y)}, ${round(z)})"
  }

  // Clasificar puntos
  def hallarPuntoMasCercano(p: Punto, medianas: IterableOnce[Punto]): Punto = {
    val it = medianas.iterator
    assert(it.nonEmpty)
    var puntoMasCercano = it.next()
    var minDistancia = p.distanciaAlCuadrado(puntoMasCercano)
    while (it.hasNext) {
      val point = it.next()
      val distancia = p.distanciaAlCuadrado(point)
      if (distancia < minDistancia) {
        minDistancia = distancia
        puntoMasCercano = point
      }
    }
    puntoMasCercano
  }

  def clasificarPar(puntos: ParSeq[Punto], medianas: ParSeq[Punto]): ParMap[Punto, ParSeq[Punto]] = {
    val grupos = puntos.groupBy(p => hallarPuntoMasCercano(p, medianas))
    grupos
  }

  def clasificarSeq(puntos: Seq[Punto], medianas: Seq[Punto]): Map[Punto, Seq[Punto]] = {
    val grupos = puntos.groupBy(p => hallarPuntoMasCercano(p, medianas))
    grupos
  }

  def calculePromedioPar(oldMean: Punto, points: ParSeq[Punto]): Punto = {
    if (points.isEmpty) oldMean
    else {
      var x = 0.0
      var y = 0.0
      var z = 0.0
      points.seq.foreach { p =>
        x += p.x
        y += p.y
        z += p.z
      }
      new Punto(x / points.length, y / points.length, z / points.length)
    }
  }

  def calculePromedioSeq(oldMean: Punto, points: Seq[Punto]): Punto = {
    if (points.isEmpty) oldMean
    else {
      var x = 0.0
      var y = 0.0
      var z = 0.0
      points.foreach { p =>
        x += p.x
        y += p.y
        z += p.z
      }
      new Punto(x / points.length, y / points.length, z / points.length)
    }
  }

  def actualizarPar(clasif: ParMap[Punto, ParSeq[Punto]], medianasViejas: ParSeq[Punto]): ParSeq[Punto] = {
    medianasViejas.map { medianaVieja =>
      clasif.get(medianaVieja) match {
        case Some(puntos) => calculePromedioPar(medianaVieja, puntos)
        case None => medianaVieja
      }
    }
  }

  def actualizarSeq(clasif: Map[Punto, Seq[Punto]], medianasViejas: Seq[Punto]): Seq[Punto] = {
    medianasViejas.map { medianaVieja =>
      clasif.get(medianaVieja) match {
        case Some(puntos) => calculePromedioSeq(medianaVieja, puntos)
        case None => medianaVieja
      }
    }
  }

  def hayConvergenciaPar(eta: Double, medianasViejas: ParSeq[Punto], medianasNuevas: ParSeq[Punto]): Boolean = {
    medianasViejas.zip(medianasNuevas).forall { case (vieja, nueva) =>
      vieja.distanciaAlCuadrado(nueva) <= eta
    }
  }

  def hayConvergenciaSeq(eta: Double, medianasViejas: Seq[Punto], medianasNuevas: Seq[Punto]): Boolean = {
    medianasViejas.zip(medianasNuevas).forall { case (vieja, nueva) =>
      vieja.distanciaAlCuadrado(nueva) <= eta
    }
  }


  @tailrec
  final def kMedianasPar(puntos: ParSeq[Punto], medianas: ParSeq[Punto], eta: Double): ParSeq[Punto] = {
    val clasificacion = clasificarPar(puntos, medianas)
    val medianasNuevas = actualizarPar(clasificacion, medianas)
    
    if (hayConvergenciaPar(eta, medianas, medianasNuevas)) {
      medianasNuevas
    } else {
      kMedianasPar(puntos, medianasNuevas, eta)
    }
  }

  @tailrec
  final def kMedianasSeq(puntos: Seq[Punto], medianas: Seq[Punto], eta: Double): Seq[Punto] = {
    val clasificacion = clasificarSeq(puntos, medianas)
    val medianasNuevas = actualizarSeq(clasificacion, medianas)
    
    if (hayConvergenciaSeq(eta, medianas, medianasNuevas)) {
      medianasNuevas
    } else {
      kMedianasSeq(puntos, medianasNuevas, eta)
    }
  }


  def generarPuntosPar(k: Int, num: Int): ParSeq[Punto] = {
    val randx = new Random(1)
    val randy = new Random(3)
    val randz = new Random(5)
    (0 until num).map { i =>
      val x = ((i + 1) % k) * 1.0 / k + randx.nextDouble() * 0.5
      val y = ((i + 5) % k) * 1.0 / k + randy.nextDouble() * 0.5
      val z = ((i + 7) % k) * 1.0 / k + randz.nextDouble() * 0.5
      new Punto(x, y, z)
    }.to(mutable.ArrayBuffer).par
  }

  def generarPuntosSeq(k: Int, num: Int): Seq[Punto] = {
    val randx = new Random(1)
    val randy = new Random(3)
    val randz = new Random(5)
    (0 until num).map { i =>
      val x = ((i + 1) % k) * 1.0 / k + randx.nextDouble() * 0.5
      val y = ((i + 5) % k) * 1.0 / k + randy.nextDouble() * 0.5
      val z = ((i + 7) % k) * 1.0 / k + randz.nextDouble() * 0.5
      new Punto(x, y, z)
    }.to(mutable.ArrayBuffer)
  }

  def inicializarMedianasPar(k: Int, puntos: ParSeq[Punto]): ParSeq[Punto] = {
    val rand = new Random(7)
    (0 until k).map(_ => puntos(rand.nextInt(puntos.length))).to(mutable.ArrayBuffer).par
  }

  def inicializarMedianasSeq(k: Int, puntos: Seq[Punto]): Seq[Punto] = {
    val rand = new Random(7)
    (0 until k).map(_ => puntos(rand.nextInt(puntos.length))).to(mutable.ArrayBuffer)
  }
}
