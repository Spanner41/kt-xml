package design.cardia.ktxml

import design.cardia.ktxml.builder.CompressedFormat
import design.cardia.ktxml.builder.PrettyFormat
import design.cardia.ktxml.builder.XmlEncoding
import design.cardia.ktxml.builder.XmlVersion
import design.cardia.ktxml.builder.document
import design.cardia.ktxml.builder.element

fun main() {
    val xml = document(XmlVersion.V1_1, XmlEncoding.UTF_8) {
        element("project") {
            "xmlns" to "http://maven.apache.org/POM/4.0.0"
            "xmlns:xsi" to "http://www.w3.org/2001/XMLSchema-instance"
            "xsi:schemaLocation" to "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"

            element("modelVersion") withText "4.0.0"
            element("groupId") withText "design.cardia"
            element("artifactId") withText "kt-xml"
            element("version") withText "0.0.1"
            element("packaging") withText "jar"
            element("name") withText "kt-xml"

            element("properties") {
                element("main.class") withText "design.cardia.ktxml.Main.kt"
                element("kotlin.version") withText "1.4.32"
            }
        }
    }

    println(xml.toXml(PrettyFormat()))
    println()
    println(xml.toXml(CompressedFormat()))
}
