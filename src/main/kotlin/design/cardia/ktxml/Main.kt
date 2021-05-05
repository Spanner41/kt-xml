package design.cardia.ktxml

import design.cardia.ktxml.builder.CompressedFormat
import design.cardia.ktxml.builder.PrettyFormat
import design.cardia.ktxml.builder.XmlEncoding
import design.cardia.ktxml.builder.XmlVersion
import design.cardia.ktxml.builder.document
import design.cardia.ktxml.builder.element
import design.cardia.ktxml.builder.text

fun main() {
    val xml = document(XmlVersion.V1_1, XmlEncoding.UTF_8) {
        element("project") {
            "xmlns" to "http://maven.apache.org/POM/4.0.0"
            "xmlns:xsi" to "http://www.w3.org/2001/XMLSchema-instance"
            "xsi:schemaLocation" to "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"

            element("modelVersion") { text("4.0.0") }

            element("groupId") { text("design.cardia") }
        }
    }

    println(xml.toXml(PrettyFormat()))
    println()
    println(xml.toXml(CompressedFormat()))
}
