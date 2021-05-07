package design.cardia.ktxml.builder

class CdataNode(val text: String) : NodeWithoutChildren {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding, indentLevel: Int): String = "<![CDATA[${text.escape()}]]>"

    private fun String.escape() = replace("]]>", "]]&gt;")
}
