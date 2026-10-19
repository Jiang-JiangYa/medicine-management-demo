import { saveAs } from "file-saver";
import { utils, SSF, write, BookType } from "xlsx";
// import { XLSXStyle } from 'xlsx-style'

function generateArray(table: any) {
  const out: any[] = [];
  const rows = table.querySelectorAll("tr");
  const ranges: any[] = [];
  for (let R = 0; R < rows.length; ++R) {
    const outRow: any[] = [];
    const row = rows[R];
    const columns = row.querySelectorAll("td");
    for (const cell of columns) {
      let colspan = cell.getAttribute("colspan");
      let rowspan = cell.getAttribute("rowspan");
      let cellValue = cell.innerText;
      if (cellValue !== "" && cellValue === +cellValue) cellValue = +cellValue;
      ranges.forEach(range => {
        if (R >= range.s.r && R <= range.e.r && outRow.length >= range.s.c && outRow.length <= range.e.c) {
          for (let i = 0; i <= range.e.c - range.s.c; ++i) outRow.push(null);
        }
      });
      if (rowspan || colspan) {
        rowspan = rowspan || 1;
        colspan = colspan || 1;
        ranges.push({
          s: {
            r: R,
            c: outRow.length
          },
          e: {
            r: R + rowspan - 1,
            c: outRow.length + colspan - 1
          }
        });
      }

      outRow.push(cellValue !== "" ? cellValue : null);

      if (colspan) for (let k = 0; k < colspan - 1; ++k) outRow.push(null);
    }
    out.push(outRow);
  }
  return [out, ranges];
}

function datenum(v: any, date1904 = null) {
  if (date1904) {
    v += 1462;
  }
  const epoch = Date.parse(v);
  return (epoch - (new Date(Date.UTC(1899, 11, 30)) as any)) / (24 * 60 * 60 * 1000);
}

function sheet_from_array_of_arrays(data: any) {
  const ws: any = {};
  const range = {
    s: {
      c: 10000000,
      r: 10000000
    },
    e: {
      c: 0,
      r: 0
    }
  };
  for (let R = 0; R !== data.length; ++R) {
    for (let C = 0; C !== data[R].length; ++C) {
      if (range.s.r > R) range.s.r = R;
      if (range.s.c > C) range.s.c = C;
      if (range.e.r < R) range.e.r = R;
      if (range.e.c < C) range.e.c = C;
      const cell: any = {
        v: data[R][C]
      };
      if (cell.v === null) continue;
      const cellRef = utils.encode_cell({
        c: C,
        r: R
      });

      if (typeof cell.v === "number") cell.t = "n";
      else if (typeof cell.v === "boolean") cell.t = "b";
      else if (cell.v instanceof Date) {
        cell.t = "n";
        cell.z = (SSF as any)._table[14];
        cell.v = datenum(cell.v);
      } else cell.t = "s";

      ws[cellRef] = cell;
    }
  }
  if (range.s.c < 10000000) ws["!ref"] = utils.encode_range(range);
  return ws;
}

class Workbook {
  public SheetNames: any[] = [];
  public Sheets: any = {};
}

function s2ab(s: any) {
  const buf = new ArrayBuffer(s.length);
  const view = new Uint8Array(buf);
  for (let i = 0; i !== s.length; ++i) view[i] = s.charCodeAt(i) & 0xff;
  return buf;
}

export function export_table_to_excel(id: any) {
  const theTable = document.getElementById(id);
  const oo = generateArray(theTable);
  const ranges = oo[1];

  const data = oo[0];
  const wsName = "SheetJS";

  const wb = new Workbook();
  const ws = sheet_from_array_of_arrays(data);

  ws["!merges"] = ranges;

  wb.SheetNames.push(wsName);
  wb.Sheets[wsName] = ws;

  const wbout = write(wb, {
    bookType: "xlsx",
    bookSST: false,
    type: "binary"
  });

  saveAs(
    new Blob([s2ab(wbout)], {
      type: "application/octet-stream"
    }),
    "test.xlsx"
  );
}

export function export_json_to_excel(
  { multiHeader = [], header, data, filename, merges = [], autoWidth = true, bookType = "xlsx" } = {
    header: {},
    data: [] as any[],
    filename: ""
  }
) {
  /* original data */
  filename = filename || "excel-list";
  data = [...data];
  data.unshift(header);

  for (let i = multiHeader.length - 1; i > -1; i--) {
    data.unshift(multiHeader[i]);
  }

  const wsName = "SheetJS";
  const wb = new Workbook();
  const ws = sheet_from_array_of_arrays(data);

  if (merges.length > 0) {
    if (!ws["!merges"]) {
      ws["!merges"] = [];
    }
    merges.forEach(item => {
      ws["!merges"].push(utils.decode_range(item));
    });
  }

  if (autoWidth) {
    const colWidth = data.map(row =>
      row.map((val: any) => {
        if (val === null) {
          return {
            wch: 10
          };
        } else if (val.toString().charCodeAt(0) > 255) {
          return {
            wch: val.toString().length * 2
          };
        } else {
          return {
            wch: val.toString().length
          };
        }
      })
    );
    const result = colWidth[0];
    for (let i = 1; i < colWidth.length; i++) {
      for (let j = 0; j < colWidth[i].length; j++) {
        if (result[j]["wch"] < colWidth[i][j]["wch"]) {
          result[j]["wch"] = colWidth[i][j]["wch"];
        }
      }
    }
    ws["!cols"] = result;
  }

  wb.SheetNames.push(wsName);
  wb.Sheets[wsName] = ws;

  const wbout = write(wb, {
    bookType: bookType as BookType,
    bookSST: false,
    type: "binary"
  });
  saveAs(
    new Blob([s2ab(wbout)], {
      type: "application/octet-stream"
    }),
    `${filename}.${bookType}`
  );
}
// 导出 第一行是title
export function exportDrugExpiryTable(headerList: any[], exportList: any[][], title: any) {
  // 1. 构建中文表头
  const headers = [headerList];
  // 2. 构建数据数组，注意数据的顺序要与表头一致
  const dataWithHeader = [
    [{ v: title, s: { fontSize: 30, bold: true }, t: "s" }], // 添加第一行文字，设置字体大小为30px
    ...headers,
    ...exportList // 其余行为表格数据
  ];
  // 3. 创建工作表
  const worksheet = utils.aoa_to_sheet(dataWithHeader);
  // 4. 创建工作簿并导出
  const workbook = utils.book_new();
  utils.book_append_sheet(workbook, worksheet, title);
  // 5. 将工作簿转换为 Excel 文件
  const excelBuffer = write(workbook, {
    bookType: "xlsx",
    type: "array"
  });
  // 6. 使用 file-saver 将 Excel 文件保存到本地
  const blob = new Blob([excelBuffer], { type: "application/octet-stream" });
  saveAs(blob, `${title}.xlsx`);
}
// 导出双表格功能
export function exportTwoExpiryTable(
  headerList1: any[], // 表头1
  headerList2: any[], // 表头2
  exportList1: any[][], // 数据列表
  exportList2: any[][], // 数据列表
  title: string, // 文件标题
  title2: string // 小标题
) {
  // 1. 构建中文表头
  const headers1 = [headerList1];
  const headers2 = [headerList2];

  // 2. 创建第一个表格数据（详细数据）
  const dataWithHeader1 = [
    [{ v: title, s: { fontSize: 30, bold: true }, t: "s" }], // 第一个表格标题
    ...headers1,
    ...exportList1 // 详细数据
  ];

  const dataWithHeader2 =
    exportList2.length > 0
      ? [
          [{ v: title2, s: { fontSize: 30, bold: true }, t: "s" }], // 第二个表格标题
          ...headers2,
          ...exportList2 // 汇总数据
        ]
      : []; // 如果第二个表格没有数据，则返回空数组

  // 3. 插入空行作为两个表格之间的分隔
  const separatorRow = Array(headerList1.length).fill(""); // 一个空行来分隔两个表格
  const combinedData = [
    ...dataWithHeader1, // 第一个表格数据
    ...(dataWithHeader2.length > 0 ? [separatorRow] : []), // 如果有第二个表格数据才加空行
    ...dataWithHeader2 // 第二个表格数据
  ];

  // 4. 创建工作表
  const worksheet = utils.aoa_to_sheet(combinedData);

  // 5. 创建工作簿并导出
  const workbook = utils.book_new();
  utils.book_append_sheet(workbook, worksheet, title);

  // 6. 将工作簿转换为 Excel 文件
  const excelBuffer = write(workbook, {
    bookType: "xlsx",
    type: "array"
  });

  // 7. 使用 file-saver 将 Excel 文件保存到本地
  const blob = new Blob([excelBuffer], { type: "application/octet-stream" });
  saveAs(blob, `${title}.xlsx`);
}
