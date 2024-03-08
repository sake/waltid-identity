package id.walt.cli

import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.output.ParameterFormatter
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.Whitespace
import com.github.ajalt.mordant.widgets.Panel
import com.github.ajalt.mordant.widgets.Text

fun t(args: Array<String>) {
    println("T() Function: $args.toString()")
}

fun main(args: Array<String>) {
    val cmd = WaltIdCmd()
    try {
        cmd.parse(args)
    } catch (e: PrintHelpMessage) {
        handlePrintHelpMessage(cmd, e)
    } catch (e: InvalidFileFormat) {
        handleInvalidFileFormat(cmd, e)
    } catch (e: MultiUsageError) {
        handleMultiUsageError(cmd, e)
    } catch (e: NoSuchOption) {
        handleNoSuchOption(cmd, e)
    } catch (e: CliktError) {
        handleCliktError(cmd, e)
    }
    // finally {
    //     exitProcess(e.statusCode)
    // }
}

fun handleCliktError(cmd: WaltIdCmd, e: CliktError) {
    printError(cmd, e)
    printUsage(cmd, e)
}

fun handleNoSuchOption(cmd: WaltIdCmd, e: NoSuchOption) {
    printError(cmd, e)
    printUsage(cmd, e)
}

fun handleMultiUsageError(cmd: WaltIdCmd, e: MultiUsageError) {
    var msgs = "Invalid command. Please, review the usage instructions bellow and try again."
    // for (error in e.errors) {
    //     if (msgs.length == 0) {
    //         // msgs = error.formatMessage(error.context!!.localization, parameterFormatter(error.context!!))
    //         msgs = "${error.localizedMessage} - ${error.message} "
    //     } else {
    //         msgs = """${msgs} ${error.toString() ?: ""}"""
    //     }
    // }
    printError(cmd, e, msgs)
    printUsage(cmd, e)


}

fun handleInvalidFileFormat(cmd: WaltIdCmd, e: InvalidFileFormat) {
    printError(cmd, e)
    printUsage(cmd, e)
}

fun handlePrintHelpMessage(cmd: WaltIdCmd, e: PrintHelpMessage) {
    cmd.echoFormattedHelp(e)
}

fun printError(cmd: CliktCommand, e: CliktError? = null, msg: String? = null) {
    println("\n")
    val msgToPrint = msg ?: e?.let { it.localizedMessage }
    cmd.terminal.println(
        Panel(
            content = Text(
                TextColors.brightRed(msgToPrint ?: e.toString()),
                whitespace = Whitespace.NORMAL,
                width = 70
            ),
            title = Text(TextColors.red("ERROR"))
        )
    )
    println("\n")
}

fun printUsage(cmd: CliktCommand, e: CliktError) {
    val ctx = (e as ContextCliktError).context
    cmd.echoFormattedHelp(PrintHelpMessage(ctx))
}

fun parameterFormatter(context: Context): ParameterFormatter {
    return object : ParameterFormatter {
        override fun formatOption(name: String): String {
            // return styleOptionName(name)
            return context.theme.style("info")(name)
        }

        override fun formatArgument(name: String): String {
            // return styleArgumentName(normalizeParameter(name))
            return context.theme.style("info")("<${name.lowercase()}>")
        }

        override fun formatSubcommand(name: String): String {
            // return styleSubcommandName(name)
            return context.theme.style("info")(name)
        }

    }
}
