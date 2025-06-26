# Techsenger TabPanePro

| Support the Project! |
|:-------------|
| This project is open-source and free to use, both commercially and non-commercially, which is why we need your help in its development. If you like it, please give it a star ⭐ on GitHub — it helps others discover the project and increases its visibility. You can also contribute, for example, by fixing bugs 🐛 or suggesting improvements 💡, see [Contributing](#contributing). If you can, financial support 💰 is always appreciated, see [Support Us](#support-us). Thank you! |

## Table of Contents
* [Overview](#overview)
* [Demo](#demo)
* [Features](#features)
* [Dependencies](#dependencies)
* [Usage](#usage)
    * [Areas](#usage-areas)
    * [Tabs Menu](#usage-tabs-menu)
    * [Tabs ScrollBar](#usage-tabs-scroll-bar)
    * [Tab Scrolling](#usage-tab-scrolling)
* [Code building](#code-building)
* [Running Demo](#running-demo)
* [License](#license)
* [Contributing](#contributing)
* [👉 Support Us](#support-us)

## Overview <a name="overview"></a>

Techsenger TabPanePro is a lightweight library that extends the standard `TabPane` in JavaFX with a set of
practical enhancements addressing common needs in real-world applications. For the development of the library,
we used the standard TabPaneSkin, carefully extracted from the OpenJFX project with a minimal set of required
classes and adapted to work independently of OpenJFX internals.

## Demo <a name="demo"></a>

![Screenshot from 2025-06-26 11-53-03](https://github.com/user-attachments/assets/a61b95e7-598d-470b-86ec-e4d07ecd8743)

![Screenshot from 2025-06-26 11-54-04](https://github.com/user-attachments/assets/e786dcce-5399-4bdb-9aa3-1bb746e32957)

## Features <a name="features"></a>

Key features include:

* Two additional areas placed before and after all tabs, available for any custom purpose.
* A sticky area, located between the tabs and the trailing area, typically used for a New Tab button.
* Ability to show the tab header area even when there are no tabs.
* Support for both the standard and custom tab menus.
* Supports tab scrolling via a ScrollBar, with four CSS-configurable positions per side.
* API for programmatic tab scrolling with scroll state tracking.
* A demo application showcasing all library features.
* Comprehensive documentation.

## Dependencies <a name="dependencies"></a>

This project will be available on Maven Central in a few weeks:

```
<dependency>
    <groupId>com.techsenger.tabpanepro</groupId>
    <artifactId>tabpanepro-core</artifactId>
    <version>${tabpanepro.version}</version>
</dependency>
```

## Usage <a name="usage"></a>

### Areas <a name="usage-areas"></a>

The library provides three optional areas that can be used if needed. You can access these areas through the skin:

```
TabPanePro tabPane = new TabPanePro();
var skin = (TabPaneProSkin) tabPane.getSkin();
var firstArea = skin.getTabHeaderFirstArea(); // CSS: .first-area
var stickyArea = skin.getTabHeaderStickyArea(); // CSS: .sticky-area
var lastArea = skin.getTabHeaderLastArea(); // CSS: .last-area
```
Note that these areas will rotate when the side of the pane is set to `RIGHT`, `BOTTOM`, or `LEFT`. However, the
library does not apply any automatic transformation or layout logic to adapt their content. This is intentional, as
there are multiple possible layout strategies, and enforcing a single approach could cause limitations or conflicts.
See `demo.css` for styling examples across different sides.

The standard `TabPane` hides its tab header area when there are no tabs. However, this behavior is not suitable if you
need to display the areas, which may contain various controls. To address this, use
`TabPaneProSkin#tabHeaderAreaPolicyProperty()`:

```
skin.setTabHeaderAreaPolicy(TabHeaderAreaPolicy.ALWAYS_VISIBLE);
```

### Tabs Menu <a name="usage-tabs-menu"></a>

The Tabs Menu allows quick selection of a specific tab. This menu is typically used when not all tabs are visible to
the user. The library supports both custom menus and the standard menu. To show the standard menu, use the method
`TabPaneProSkin#showTabsMenu(Node)`. To determine whether this menu should be shown, use
`TabPaneProSkin#headersRegionOverflowedProperty()`.

### Tabs ScrollBar <a name="usage-tabs-scroll-bar"></a>

To enable scrolling of tabs using a `ScrollBar`, first set the `tabScrollBarEnabled` property of the skin
class to `true`. As a result, the `ScrollBar` will be added to the tab header area, but it will remain invisible by
default. It only becomes visible when the tabs do not fit and the user hovers the cursor over the header area.

The "vertical" position of the `ScrollBar` can be configured using two CSS properties:

* `-tpp-header-position` accepts the values `above_tabs` and `below_tabs` and determines whether the `ScrollBar` is
placed above or below the tabs.
* `-tpp-stick-to-edge` accepts the values `true` or `false` and indicates whether the `ScrollBar` is pinned to the edge.
When set to true, the tab header area's padding is ignored. When set to false, the `ScrollBar`'s position depends on
the parent's padding.

Example:

```
.tab-pane-pro > .tab-header-area > .tab-scroll-bar {
    -tpp-header-position: above_tabs;
    -tpp-stick-to-edge: false;
}
```

### Tab Scrolling <a name="usage-tab-scrolling"></a>

The library provides all necessary APIs for scrolling tabs and obtaining information about the current scroll state.
To scroll the tabs, use the method `TabPaneProSkin#scrollTabHeadersBy(double)`. To determine the scroll state, use the
three properties: `TabPaneProSkin#headersRegionWidthProperty()`, `TabPaneProSkin#headersRegionOffsetProperty()`, and
`TabPaneProSkin#headersClipWidthProperty()`. See the demo for an example implementation.

## Code Building <a name="code-building"></a>

To build the library use standard Git and Maven commands:

    git clone https://github.com/techsenger/tabpanepro
    cd tabpanepro
    mvn clean install

## Running Demo <a name="running-demo"></a>

To run the demo execute the following commands in the root of the project:

    cd tabpanepro-demo
    mvn javafx:run

Please note, that debugger settings are in `tabpanepro-demo/pom.xml` file.

## License <a name="license"></a>

Techsenger TabPanePro is licensed under the GNU General Public License version 2, with the Classpath Exception.

## Contributing <a name="contributing"></a>

We welcome all contributions. You can help by reporting bugs, suggesting improvements, or submitting pull requests
with fixes and new features. If you have any questions, feel free to reach out — we’ll be happy to assist you.

## 👉 Support Us <a name="support-us"></a>

You can support us financially through [GitHub Sponsors](https://github.com/sponsors/techsenger). Your
contribution directly helps us keep our open-source projects active, improve their features, and offer ongoing support.
Besides, we offer multiple sponsorship tiers, with different rewards.

