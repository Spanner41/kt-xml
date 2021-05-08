package design.cardia.ktxml.model

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import design.cardia.ktxml.printer.PrettyFormat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class NodeWithAttributesTest {
    class DummyNode : NodeWithAttributes() {
        fun attributes() = attributes
        fun valueOfAttributesToXml() = attributesToXml(PrettyFormat(), XmlVersion.V1_1)
    }

    @Test
    fun `sets an attribute with lambda`() {
        DummyNode().apply {
            "test" { 1 }

            val result = attributes()["test"]?.invoke()
            assertThat(result).isEqualTo(1)
        }
    }

    @Test
    fun `sets an attribute with value`() {
        DummyNode().apply {
            "test" to 1

            val result = attributes()["test"]?.invoke()
            assertThat(result).isEqualTo(1)
        }
    }

    @Test
    fun `converts attributes to XML string`() {
        DummyNode().apply {
            "height" { 1 }
            "width" { "2px" }
            "x" to 0
            "y" to BigDecimal(5)

            assertThat(valueOfAttributesToXml()).isEqualTo(""" height="1" width="2px" x="0" y="5"""")
        }
    }

    @Test
    fun `throws IllegalArgumentException if key is invalid`() {
        listOf("1height", "a space", "").forEach {
            assertThat {
                DummyNode().apply {
                    it to 1
                }
            }.isFailure().hasMessage("$it is not a valid attribute key")
        }

        listOf("_name", "namespace:attr", "name1").forEach {
            assertThat {
                DummyNode().apply {
                    it to 1
                }
            }.isSuccess()
        }
    }
}
