package design.cardia.ktxml.printer

import design.cardia.ktxml.model.CdataNode
import design.cardia.ktxml.model.Comment
import design.cardia.ktxml.model.Document
import design.cardia.ktxml.model.Element
import design.cardia.ktxml.model.Node
import design.cardia.ktxml.model.ProcessingInstructionElement
import design.cardia.ktxml.model.Text
import design.cardia.ktxml.model.XmlVersion
import java.lang.RuntimeException

class XmlPrinter(private val elementPrinter: ElementPrinter = ElementPrinter()) {
    fun print(document: Document, format: XmlFormat) = with(document) {
        val visitor = PrintVisitor(elementPrinter, format, version)

        visitor.print(xmlInstructionElement, 0) +
            format.lineSeparator +
            visitor.print(child, 0)
    }

    private class PrintVisitor(
        private val elementPrinter: ElementPrinter,
        private val format: XmlFormat,
        private val version: XmlVersion
    ) {
        fun print(node: Node, indentLevel: Int): String =
            when (node) {
                is Element -> elementPrinter.print(
                    node,
                    format,
                    version,
                    node.newline(format, indentLevel + 1),
                    node.newline(format, indentLevel),
                    visitFunction(indentLevel + 1)
                )
                is Text -> format.escape(node.text, version)
                is ProcessingInstructionElement -> "<?${node.type}${node.attributesToXml(format, version)} ?>"
                is Comment -> "<!-- ${node.text} -->"
                is CdataNode -> "<![CDATA[${node.text.escapeCdata()}]]>"
                else -> throw RuntimeException("Implement handler for ${node::class.simpleName}")
            }

        private fun visitFunction(indentLevel: Int): Node.() -> String = { print(this, indentLevel) }

        private fun Node.newline(format: XmlFormat, indentLevel: Int) =
            if (this !is Text || format.putTextOnNewLine) {
                format.lineSeparator + format.indentString.repeat(indentLevel)
            } else {
                ""
            }

        private fun String.escapeCdata() = replace("]]>", "]]&gt;")
    }
}
