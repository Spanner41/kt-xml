package design.cardia.ktxml.builder

class CdataNode(private val text: String) : Node {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding): String = "<![CDATA[${text.escape()}]]>"

    private fun String.escape() = replace("]]>", "]]&gt;")
}
