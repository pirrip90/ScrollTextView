## 介绍
Android滚动文字内容滚动
包括:内容分多行滚动,内容只滚动单行,内容单行显示不下自带省略号

## Demo
![demo](https://github.com/pirrip90/ScrollTextView/blob/master/screenshot/scrolltextview.gif)

## 方法属性
|方法名|参数|描述|
|:---:|:---:|:---:|
| textColor | color |文字颜色(默认颜色为黑色)
| textSize | dimension |文字大小(默认文字大小为16sp)
| singleLine | boolean |是否单行(默认为单行)
| ellipsis | boolean |单行显示不下时是否带省略号(只在单行模式下有效,默认为单行显示不下带省略号)

## 方法

|属性名|参数类型|描述|
|:---:|:---:|:---:|
| setTextContent | List<String> list |设置滚动内容
| setTextContent | List<String> list, List<OnScrollClickListener> listeners  |设置滚动内容以及内容点击事件
| setTextColor | int textcolor |设置文字颜色
| setTextSize | float textsize |设置文字大小
| startTextScroll | 无 |文字开始自动滚动,初始化会自动调用该方法(适当的时机调用)
| stopTextScroll | 无 |文字暂停滚动(适当的时机调用)

## xml使用例子

```xml
    <com.xubo.scrolltextview.ScrollTextView
        xmlns:scroll_text="http://schemas.android.com/apk/res-auto"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_vertical"
        android:padding="5dp"
        scroll_text:ellipsis="false"
        scroll_text:singleLine="false"
        scroll_text:textColor="@android:color/white"
        scroll_text:textSize="14sp" />
```

## 使用
#### gradle
```gradle
repositories {

}
```



