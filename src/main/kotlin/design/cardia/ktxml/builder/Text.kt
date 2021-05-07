package design.cardia.ktxml.builder

open class Text(val text: String = "") : NodeWithoutChildren {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding, indentLevel: Int): String =
        getSeparator(format, indentLevel) + format.escape(text, version)

    private fun getSeparator(format: XmlFormat, indentLevel: Int) =
         if (format is PrettyFormat && format.textOnNewLine) format.lineSeparator + format.indentString.repeat(indentLevel) else ""
}
