package design.cardia.ktxml.builder

interface Node {
    fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding): String
}
