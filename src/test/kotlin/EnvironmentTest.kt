import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import tools.Environment

class EnvironmentTest {

    @Test
    fun testSimpleWrongProgramAnswer() {

        val environment = Environment(
                "sub hello\n" +
                        "    print somevar\n" +
                        "    set somevar 42\n" +
                        "    print somevar\n" +
                        "    print wtf\n" +
                        "\n" +
                        "sub main\n" +
                        "    set somevar 13\n" +
                        "    call hello\n" +
                        "    set smth 422\n" +
                        "    print 1337\n" +
                        "    call wtf\n" +
                        "\n" +
                        "sub wtf\n" +
                        "    call hello",
                false,
                false)

        // check answer
        Assertions.assertEquals("13\n42\n", environment.getExecLog())

        // check errors
        Assertions.assertEquals("Can't resolve variable [wtf] (line #5)\n" +
                "Program is finished\n", environment.getErrLog())
    }

    @Test
    fun testSimpleCorrectProgramAnswer()
    {
        val environment = Environment(
                "sub main\n" +
                "    set a 1\n" +
                "    call foo\n" +
                "    print a\n" +
                "\n" +
                "sub foo\n" +
                "    set a 2",
                false,
                false)

        // check answer
        Assertions.assertEquals("2\n", environment.getExecLog())

        // check errors
        Assertions.assertEquals("", environment.getErrLog())
    }

}