package design.cardia.ktxml.printer

import design.cardia.ktxml.builder.CdataNode
import design.cardia.ktxml.builder.Comment
import design.cardia.ktxml.builder.Document
import design.cardia.ktxml.builder.Element
import design.cardia.ktxml.builder.Node
import design.cardia.ktxml.builder.PrettyFormat
import design.cardia.ktxml.builder.ProcessingInstructionElement
import design.cardia.ktxml.builder.Text
import design.cardia.ktxml.builder.XmlEncoding
import design.cardia.ktxml.builder.XmlFormat
import design.cardia.ktxml.builder.XmlVersion
import java.lang.RuntimeException

class XmlPrinter {
    val elementPrinter = ElementPrinter()

    fun print(document: Document, format: XmlFormat) =
        document.print(
            format,
            newline(format, 0) + document.child.print(format, document.version, document.encoding, 0)
        )

    private fun Document.print(format: XmlFormat, content: String) =
        "${xmlInstructionElement.print(format, version, encoding, 0)}${content}"

    private fun Node.print(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding, indentLevel: Int): String =
        when (this) {
            is Element -> {
                if (elementPrinter.shouldCollapse(this, format)) {
                    elementPrinter.selfClosingTag(this, format, version)
                } else {
                    elementPrinter.openTag(this, format, version) +
                        childrenToXml(format, version, encoding, indentLevel + 1) +
                        elementPrinter.closeTag(this)
                }
            }
            is Text -> newlineForText(format, indentLevel) + format.escape(text, version)
            is ProcessingInstructionElement -> "<?$type${attributesToXml(format, version)}?>"
            is Comment -> "<!-- $text -->"
            is CdataNode -> "<![CDATA[${text.escapeCdata()}]]>"
            else -> throw RuntimeException("Implement handler for ${this::class.simpleName}")
        }

    private fun Node.childrenToXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding, indentLevel: Int): String {
        if (children().size == 1 && children()[0] is Text && format is PrettyFormat && !format.textOnNewLine) {
            return children()[0].print(format, version, encoding, indentLevel).ifBlank { "" }
        }

        return children().joinToString(
            separator = newline(format, indentLevel),
            prefix = newline(format, indentLevel),
            postfix = newline(format, indentLevel - 1)
        ) { it.print(format, version, encoding, indentLevel) }.ifBlank { "" }
    }

    private fun newline(format: XmlFormat, indentLevel: Int) =
        format.lineSeparator + if (format is PrettyFormat) format.indentString.repeat(indentLevel) else ""

    private fun newlineForText(format: XmlFormat, indentLevel: Int) =
         if (format is PrettyFormat && format.textOnNewLine) format.lineSeparator + format.indentString.repeat(indentLevel) else ""

    private fun String.escapeCdata() = replace("]]>", "]]&gt;")
}