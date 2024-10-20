package com.mikicorp.step.msdocs

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.apache.poi.util.Units
import org.apache.poi.wp.usermodel.HeaderFooterType
import org.apache.poi.xwpf.usermodel.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.math.BigInteger
import java.util.stream.DoubleStream

class MSDocsUI(val context: Context?) {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UI(
        onMSDocs: () -> Unit,
        onPDFDocs: () -> Unit,
        onOCR: () -> Unit,
        onAIOCR: () -> Unit,
        onClose: () -> Unit
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.create_test_source),
                            textAlign = TextAlign.Center
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        titleContentColor = MaterialTheme.colorScheme.secondary,
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            onMSDocs()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(15.dp, 0.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.ms_office_docs),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Button(
                        onClick = {
                            onPDFDocs()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(15.dp, 0.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.pdf_docs),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Button(
                        onClick = {
                            onOCR()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(15.dp, 0.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.ocr),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Button(
                        onClick = {
                            onAIOCR()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(15.dp, 0.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.ai_ocr),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    actions = {
                        Button(
                            onClick = {
                                onClose()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(15.dp, 0.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.close),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        )
    }
}


import org.apache.poi.xwpf.usermodel.*
import org.apache.poi.wp.usermodel.HeaderFooterType
import org.apache.poi.util.Units

object CreateWordHeaderFooterTable {
    private const val TWIPS_PER_INCH = 1440

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        XWPFDocument().use { doc ->
            FileOutputStream("./CreateWordHeaderFooterTable.docx").use { out ->

                // the body content
                var paragraph: XWPFParagraph = doc.createParagraph()
                var run: XWPFRun = paragraph.createRun()
                run.setText("The Body:")

                paragraph = doc.createParagraph()
                run = paragraph.createRun()
                run.setText("Lorem ipsum....")

                // the header content
                // create header start
                val header: XWPFHeader = doc.createHeader(HeaderFooterType.DEFAULT)


                // create table in header
                val table: XWPFTable = header.createTable(1, 3)
                // table spans full width
                table.setWidth("100%")
                // table has no borders
                table.removeBorders()
                // column widths
                val dWidthsPCT = doubleArrayOf(20.0, 60.0, 20.0)
                val stream =
                    DoubleStream.of(*dWidthsPCT).boxed()
                        .map { d: Double -> "$d%" }
                val widthsPCT =
                    stream.toArray<String> { _Dummy_.__Array__() }

                /*
                 * Create CTTblGrid for this table with widths of the 3 columns.
                 * Necessary for Libreoffice/Openoffice to accept the column widths.
                 */
                // only relations of column widths
                // f1rst col
                table.getCTTbl().addNewTblGrid().addNewGridCol()
                    .setW(
                        BigInteger.valueOf(
                            (dWidthsPCT[0] / 100.0 * TWIPS_PER_INCH).toInt()
                                .toLong()
                        )
                    )
                // second col
                table.getCTTbl().getTblGrid().addNewGridCol()
                    .setW(
                        BigInteger.valueOf(
                            (dWidthsPCT[1] / 100.0 * TWIPS_PER_INCH).toInt()
                                .toLong()
                        )
                    )
                // third col
                table.getCTTbl().getTblGrid().addNewGridCol()
                    .setW(
                        BigInteger.valueOf(
                            (dWidthsPCT[2] / 100.0 * TWIPS_PER_INCH).toInt()
                                .toLong()
                        )
                    )
                // get frist table row
                var row: XWPFTableRow = table.getRow(0)
                if (row == null) row = table.createRow()
                // set column widths
                var cell: XWPFTableCell
                for (i in 0..2) {
                    cell = row.getCell(i)
                    if (cell == null) cell = row.createCell()
                    cell.setWidth(widthsPCT[i])
                }
                // set cell contents
                // left cell
                cell = row.getCell(0)
                paragraph = if (cell.getParagraphs().size() > 0) {
                    cell.getParagraphs().get(0)
                } else {
                    cell.addParagraph()
                }
                paragraph.setAlignment(ParagraphAlignment.LEFT)
                run = paragraph.createRun()
                var pictureIn = FileInputStream("./leftLogo.png")
                run.addPicture(
                    pictureIn,
                    XWPFDocument.PICTURE_TYPE_PNG,
                    "leftLogo.png",
                    Units.toEMU(80),
                    Units.toEMU(80)
                )
                // right cell
                cell = row.getCell(2)
                paragraph = if (cell.getParagraphs().size() > 0) {
                    cell.getParagraphs().get(0)
                } else {
                    cell.addParagraph()
                }
                paragraph.setAlignment(ParagraphAlignment.RIGHT)
                run = paragraph.createRun()
                pictureIn = FileInputStream("./rightLogo.png")
                run.addPicture(
                    pictureIn,
                    XWPFDocument.PICTURE_TYPE_PNG,
                    "rightLogo.png",
                    Units.toEMU(80),
                    Units.toEMU(80)
                )
                // middle cell
                cell = row.getCell(1)
                paragraph = if (cell.getParagraphs().size() > 0) {
                    cell.getParagraphs().get(0)
                } else {
                    cell.addParagraph()
                }
                paragraph.setAlignment(ParagraphAlignment.CENTER)
                run = paragraph.createRun()
                run.setText("LOREM IPSUM SEMIT DOLOR SIT AMET")
                run.addBreak()
                run.setText("consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata")

                // the footer content
                // create footer start
                val footer: XWPFFooter = doc.createFooter(HeaderFooterType.DEFAULT)
                paragraph = footer.getParagraphArray(0)
                if (paragraph == null) paragraph = footer.createParagraph()
                paragraph.setAlignment(ParagraphAlignment.CENTER)
                run = paragraph.createRun()
                run.setText("The Footer:")


                // write out
                doc.write(out)
            }
        }
    }
}