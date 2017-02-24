#SmoothCheckBox


### Attrs 属性
|attr|format|description|
|---|:---|:---:|
|duration|integer|动画持续时间|
|stroke_width|dimension|未选中时边框宽度|
|color_tick|color|对勾颜色|
|color_checked|color|选中时填充颜色|
|color_unchecked|color|未选中时填充颜色|
|color_unchecked_stroke|color|未选中时边框颜色|
|isCheck|boolean|选中状态|

相对应有 JAVA方法；

```

    public void setmCheckedColor(int mCheckedColor) {
        this.mCheckedColor = mCheckedColor;
    }

    public void setmUnCheckedColor(int mUnCheckedColor) {
        this.mUnCheckedColor = mUnCheckedColor;
    }

    public void setmChecked(boolean mChecked) {
        this.mChecked = mChecked;
    }

    public void setmUncheckStrokeColor(int mUncheckStrokeColor) {
        this.mUncheckStrokeColor = mUncheckStrokeColor;
    }

    public void setmStrokeWidth(float mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
    }

    public void setmAnimDuration(int mAnimDuration) {
        this.mAnimDuration = mAnimDuration;
    }

    public void setTickColor(int tickColor) {
        this.tickColor = tickColor;
    }
    
```

##使用
```
SmoothCheckBox scb = (SmoothCheckBox) findViewById(R.id.scb);
//        scb.setChecked(boolean checked);
//        scb.setChecked(boolean checked, boolean animate);
scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
        Log.d("SmoothCheckBox", String.valueOf(isChecked));
    }
});
```

## 引用
```groovy
// 项目引用
dependencies {
    compile 'com.github.LidongWen:SmoothCheckBox:1.0.0'
}

// 根目录下引用

allprojects {
    repositories {
        jcenter()
        maven { url "https://www.jitpack.io" }
    }
}
```
> ## **V1.0.0** </br>
> 实现酷炫选择控件

### 记录一下

> 使用 pathMeasure 处理"勾"的路径，可达成 SVG 效果 ,但拐弯处不够圆滑 </br>
  动画视觉效果 参考别人 </br>
  渲染还没做层级优化; </br>
