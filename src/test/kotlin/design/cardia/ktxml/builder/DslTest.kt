package design.cardia.ktxml.builder

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsAll
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.prop
import design.cardia.ktxml.model.CdataNode
import design.cardia.ktxml.model.Comment
import design.cardia.ktxml.model.Element
import design.cardia.ktxml.model.ProcessingInstructionElement
import design.cardia.ktxml.model.Text
import design.cardia.ktxml.model.XmlEncoding
import design.cardia.ktxml.model.XmlVersion
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class DslTest {
    @Test
    fun `builds a document`() {
        val doc = document(XmlVersion.V1_0, XmlEncoding.UTF_8) {
            element("test")
        }

        with(doc) {
            assertThat(version).isEqualTo(XmlVersion.V1_0)
            assertThat(encoding).isEqualTo(XmlEncoding.UTF_8)
            assertThat(child).isInstanceOf(Element::class)
                .prop("type", Element::type).isEqualTo("test")
        }
    }

    @Test
    fun `builds an element`() {
        val element = element("test") {
            "version" to 1
        }

        with(element) {
            assertThat(type).isEqualTo("test")
            assertThat(this["version"]).isEqualTo(1)
        }
    }

    @Nested
    inner class WithElement {
        @Test
        fun `adds a child element`() {
            val element = element("test") {
                element("child") {
                    "version" to 1
                }
            }

            with(element) {
                assertThat(children.size).isEqualTo(1)
                assertThat(children[0]).isInstanceOf(Element::class)
                    .prop("version") { it["version"] }.isEqualTo(1)
            }
        }

        @Test
        fun `adds a child comment`() {
            val element = element("test") {
                comment("comment")
            }

            with(element) {
                assertThat(children.size).isEqualTo(1)
                assertThat(children[0]).isInstanceOf(Comment::class)
                    .prop("text") { it.text }.isEqualTo("comment")
            }
        }

        @Test
        fun `adds a child cdata element`() {
            val element = element("test") {
                cdata("cdata")
            }

            with(element) {
                assertThat(children.size).isEqualTo(1)
                assertThat(children[0]).isInstanceOf(CdataNode::class)
                    .prop("text") { it.text }.isEqualTo("cdata")
            }
        }

        @Test
        fun `adds a child processing instruction`() {
            val element = element("test") {
                processingInstruction("child") {
                    "version" to 1
                }
            }

            with(element) {
                assertThat(children.size).isEqualTo(1)
                assertThat(children[0]).isInstanceOf(ProcessingInstructionElement::class)
                    .prop("version") { it["version"] }.isEqualTo(1)
            }
        }

        @Test
        fun `adds a text`() {
            val element = element("test").withText("test")

            with(element) {
                assertThat(children.size).isEqualTo(1)
                assertThat(children[0]).isInstanceOf(Text::class)
                    .prop("text") { it.text }.isEqualTo("test")
            }
        }

        @Test
        fun `adds attributes`() {
            val attributes = listOf("test" to { 1 }, "hello" to { "world" })
            val element = element("test").attributes(attributes.toMap())

            with(element) {
                assertThat(children.size).isEqualTo(0)
                assertThat(attributes).containsAll(*attributes.toTypedArray())
            }
        }
    }
}
