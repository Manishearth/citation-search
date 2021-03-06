package net.tqft.citationsearch

import org.jboss.netty.handler.codec.http.{ HttpRequest, HttpResponse }
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.{ Http, Response }
import com.twitter.finagle.Service
import com.twitter.util.Future
import java.net.InetSocketAddress
import util.Properties
import java.net.URI
import org.jboss.netty.handler.codec.http.QueryStringDecoder
import java.net.URL
import scala.io.Source
import argonaut._
import Argonaut._

object Web {
  def main(args: Array[String]) {
    val port = Properties.envOrElse("PORT", "8080").toInt
    println("Starting on port:" + port)
    ServerBuilder()
      .codec(Http())
      .name("citationsearch")
      .bindTo(new InetSocketAddress(port))
      .build(new ResolverService)
    println("Started citation-search.")

  }
}

class ResolverService extends Service[HttpRequest, HttpResponse] {
  //  Future(Search.query("warming up ..."))

  def apply(req: HttpRequest): Future[HttpResponse] = {
    val response = Response()

    val parameters = new QueryStringDecoder(req.getUri()).getParameters
    import scala.collection.JavaConverters._
    val callback = Option(parameters.get("callback")).map(_.asScala.headOption).flatten
    val query = Option(parameters.get("q")).map(_.asScala.headOption).flatten.getOrElse("")
    val results = Search.query(query)

    response.setStatusCode(200)

    val json = {
      import argonaut._, Argonaut._
      results.asJson.spaces2
    }
//    val json = results.map({ case (c, q) => f"""   { "MRNumber": ${c.MRNumber}, "title": "${c.title}", "authors": "${c.authors}", "cite": "${c.cite}", "url": "${c.url}", ${c.pdf.map(p => f""""pdf": "$p", """).getOrElse("")}${c.free.map(f => f""""free": "$f", """).getOrElse("")}"best": "${c.best}", "score": $q } """.replaceAllLiterally("\\", "\\\\") }).mkString(s"""{ "query": "$query",\n  "results": [\n""", ",\n", "  ]\n}")
    callback match {
      case Some(c) => {
        response.setContentType("application/javascript")
        response.contentString = c + "(" + json + ");"
      }
      case None => {
        response.setContentType("application/json")
        response.contentString = json
      }
    }

    Future(response)
  }
}