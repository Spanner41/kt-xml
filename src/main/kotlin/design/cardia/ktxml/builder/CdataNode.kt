package design.cardia.ktxml.builder

class CdataNode(private val text: String): XmlContent() {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding): String = "<![CDATA[$text]]>"
}