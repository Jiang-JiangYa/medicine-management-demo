export interface Rules {
  trigger: string;
  enum: string;
  len?: number;
  max?: number;
  message: string;
  min?: number;
  pattern: string;
  required: boolean;
  type: string;
}

export interface WidgetForm {
  title: string;
  list: any[];
  config: {
    size: string;
    hideRequiredAsterisk: boolean;
    labelWidth: number;
    labelPosition: string;
    tabName: string;
  };
}

const rules: Rules = {
  trigger: "blur",
  enum: "",
  len: undefined,
  max: undefined,
  message: "",
  min: undefined,
  pattern: "",
  required: false,
  type: "any"
};

export const widgetForm: WidgetForm = {
  title: "",
  list: [],
  config: {
    size: "default",
    hideRequiredAsterisk: false,
    labelWidth: 100,
    labelPosition: "right",
    tabName: "逻辑跳转"
  }
};

export const basicComponents = [
  {
    label: "单行文本",
    type: "input",
    index: 0,
    options: {
      width: "100%",
      defaultValue: "",
      placeholder: "",
      maxlength: null,
      prefix: "",
      suffix: "",
      prepend: "",
      append: "",
      goUp: 0,
      goUpName: "0",
      isShow: false,
      clearable: false,
      readonly: false,
      rules
    }
  },
  {
    label: "密码框",
    type: "password",
    index: 0,
    options: {
      width: "100%",
      defaultValue: "",
      placeholder: "",
      maxlength: null,
      prefix: "",
      suffix: "",
      prepend: "",
      append: "",
      showPassword: true,
      goUp: 0,
      goUpName: "0",
      isShow: false,
      clearable: false,
      readonly: false,
      rules
    }
  },
  {
    label: "多行文本",
    type: "textarea",
    index: 0,
    options: {
      width: "100%",
      defaultValue: "",
      placeholder: "",
      maxlength: null,
      rows: 4,
      autosize: false,
      showWordLimit: false,
      goUp: 0,
      goUpName: "0",
      isShow: false,
      clearable: false,
      readonly: false,
      rules
    }
  },
  {
    label: "计数器",
    type: "number",
    index: 0,
    options: {
      width: "",
      defaultValue: 0,
      min: 0,
      max: 100,
      step: 1,
      goUp: 0,
      goUpName: 0,
      isShow: false,
      rules
    }
  },
  {
    label: "单选框组",
    type: "radio",
    index: 0,
    options: {
      defaultValue: "",
      width: "",
      inline: true,
      remote: false,
      showLabel: false,
      remoteFunc: "https://raw.githubusercontent.com/fuchengwei/vue-form-create/master/mock/mock.json",
      options: [
        {
          value: "Option 1",
          label: "Option 1"
        },
        {
          value: "Option 2",
          label: "Option 2"
        },
        {
          value: "Option 3",
          label: "Option 3"
        }
      ],
      remoteOptions: [],
      props: {
        value: "value",
        label: "label"
      },
      goUp: 0,
      goUpName: "0",
      isShow: false,
      rules
    }
  },
  {
    label: "多选框组",
    type: "checkbox",
    index: 0,
    options: {
      defaultValue: [],
      width: "",
      inline: true,
      remote: false,
      showLabel: false,
      remoteFunc: "https://raw.githubusercontent.com/fuchengwei/vue-form-create/master/mock/mock.json",
      options: [
        {
          label: "Option 1",
          value: "Option 1"
        },
        {
          label: "Option 2",
          value: "Option 2"
        },
        {
          label: "Option 3",
          value: "Option 3"
        }
      ],
      remoteOptions: [],
      props: {
        value: "value",
        label: "label"
      },
      goUp: 0,
      goUpName: 0,
      isShow: false,
      rules
    }
  },
  {
    label: "时间选择器",
    type: "time",
    index: 0,
    options: {
      defaultValue: "",
      width: "",
      placeholder: "请选择时间",
      format: "HH:mm:ss",
      valueFormat: "HH:mm:ss",
      readonly: false,
      editable: true,
      clearable: true,
      goUp: 0,
      goUpName: "0",
      isShow: false,
      rules
    }
  },
  {
    label: "日期选择器",
    type: "date",
    index: 0,
    options: {
      defaultValue: "",
      width: "",
      placeholder: "请选择时间",
      format: "YYYY-MM-DD",
      readonly: false,
      editable: true,
      clearable: true,
      goUp: 0,
      goUpName: 0,
      isShow: false,
      rules
    }
  },
  {
    label: "评分",
    type: "rate",
    index: 0,
    options: {
      defaultValue: 0,
      max: 5,
      allowHalf: false,
      goUp: 0,
      goUpName: "0",
      isShow: false,
      rules
    }
  },
  {
    label: "下拉选择框",
    type: "select",
    index: 0,
    options: {
      defaultValue: "",
      width: "200px",
      multiple: false,
      placeholder: "",
      remote: false,
      showLabel: false,
      filterable: false,
      clearable: false,
      goUp: 0,
      goUpName: "0",
      isShow: false,
      props: {
        label: "label",
        value: "value"
      },
      options: [
        {
          label: "Option 1",
          value: "Option 1"
        },
        {
          label: "Option 2",
          value: "Option 2"
        },
        {
          label: "Option 3",
          value: "Option 3"
        }
      ],
      remoteOptions: [],
      remoteFunc: "https://raw.githubusercontent.com/fuchengwei/vue-form-create/master/mock/mock.json",
      rules
    }
  },
  {
    label: "开关",
    type: "switch",
    index: 0,
    options: {
      defaultValue: false,
      goUp: 0,
      goUpName: "0",
      isShow: false,
      activeText: "",
      inactiveText: "",
      rules
    }
  },
  {
    label: "滑块",
    type: "slider",
    index: 0,
    options: {
      defaultValue: 0,
      width: "",
      min: 0,
      max: 100,
      step: 1,
      goUp: 0,
      goUpName: "0",
      isShow: false,
      range: false,
      rules
    }
  },
  {
    label: "文字",
    type: "text",
    index: 0,
    options: {
      defaultValue: "This is a text"
    }
  },
  {
    label: "表格",
    type: "table",
    index: 0,
    options: {
      defaultValue: [],
      width: "200px",
      placeholder: "",
      goUp: 0,
      goUpName: "0",
      isShow: false,
      clearable: false,
      filterable: false,
      remote: true,
      remoteOptions: [],
      remoteFunc: "https://raw.githubusercontent.com/fuchengwei/vue-form-create/master/mock/mock.json",
      rules
    }
  }
];

export const advanceComponents = [
  {
    label: "图片",
    type: "img-upload",
    index: 0,
    options: {
      defaultValue: [],
      name: "file",
      action: "http://example.com/upload",
      method: "post",
      listType: "text",
      accept: "image/*",
      limit: 3,
      multiple: false,
      goUp: 0,
      goUpName: "0",
      isShow: false,
      rules
    }
  },
  {
    label: "富文本编辑器",
    type: "richtext-editor",
    index: 0,
    options: {
      defaultValue: "",
      width: "",
      goUp: 0,
      goUpName: "0",
      isShow: false
    }
  },
  {
    label: "级联选择器",
    type: "cascader",
    index: 0,
    options: {
      defaultValue: [],
      width: "200px",
      placeholder: "",
      goUp: 0,
      goUpName: "0",
      isShow: false,
      clearable: false,
      filterable: false,
      remote: true,
      remoteOptions: [],
      props: {
        label: "label",
        value: "value",
        children: "children"
      },
      remoteFunc: "https://raw.githubusercontent.com/fuchengwei/vue-form-create/master/mock/mock.json",
      rules
    }
  }
];

export const layoutComponents = [
  {
    label: "栅格布局",
    type: "grid",
    columns: [
      {
        span: 12,
        list: []
      },
      {
        span: 12,
        list: []
      }
    ],
    options: {
      gutter: 0,
      justify: "start",
      align: "top"
    }
  },
  {
    label: "表格",
    type: "grid",
    columns: [
      {
        span: 12,
        list: []
      },
      {
        span: 12,
        list: []
      }
    ],
    options: {
      gutter: 0,
      justify: "start",
      align: "top"
    }
  }
];

export const fieldComponents = [
  {
    label: "姓名",
    type: "input",
    index: 0,
    options: {
      width: "100%",
      defaultValue: "",
      placeholder: "",
      maxlength: null,
      prefix: "",
      suffix: "",
      prepend: "",
      append: "",
      goUp: 0,
      goUpName: "0",
      isShow: false,
      clearable: false,
      readonly: false,
      rules
    }
  },
  {
    label: "性别",
    type: "input",
    index: 0,
    options: {
      width: "100%",
      defaultValue: "",
      placeholder: "",
      maxlength: null,
      prefix: "",
      suffix: "",
      prepend: "",
      append: "",
      goUp: 0,
      goUpName: "0",
      isShow: false,
      clearable: false,
      readonly: false,
      rules
    }
  },
  {
    label: "年龄",
    type: "input",
    index: 0,
    options: {
      width: "100%",
      defaultValue: "",
      placeholder: "",
      maxlength: null,
      prefix: "",
      suffix: "",
      prepend: "",
      append: "",
      goUp: 0,
      goUpName: "0",
      isShow: false,
      clearable: false,
      readonly: false,
      rules
    }
  },
  {
    label: "入院时间",
    type: "time",
    index: 0,
    options: {
      defaultValue: "",
      width: "",
      placeholder: "请选择时间",
      format: "YYYY-MM-DD",
      readonly: false,
      editable: true,
      clearable: true,
      goUp: 0,
      goUpName: "0",
      isShow: false,
      rules
    }
  }
];
