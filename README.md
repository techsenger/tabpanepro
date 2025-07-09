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
    * [Tab Drag and Drop](#usage-tab-drag-and-drop)
* [Code building](#code-building)
* [Running Demo](#running-demo)
* [License](#license)
* [Contributing](#contributing)
* [👉 Support Us](#support-us)

## Overview <a name="overview"></a>

Techsenger TabPanePro is a lightweight library that extends the standard `TabPane` in JavaFX with a set of
practical enhancements addressing common needs in real-world applications. To develop the library, we used
the standard TabPaneSkin, carefully extracted from the OpenJFX project along with a minimal set of required
classes.

## Demo <a name="demo"></a>

![t1](https://github.com/user-attachments/assets/17a6ee70-c724-4bf7-b882-2b8a0a064bd7)

![t2](https://github.com/user-attachments/assets/b41596f7-ddd0-4a94-a932-27faa9e5d2ef)

## Features <a name="features"></a>

Key features include:

* Two additional areas placed before and after all tabs, available for any custom purpose.
* A sticky area, located between the tabs and the trailing area, typically used for a New Tab button.
* Ability to show the tab header area even when there are no tabs.
* Support for both the standard and custom tab menus.
* Supports tab scrolling via a ScrollBar, with four CSS-configurable positions per side.
* API for programmatic tab scrolling with scroll state tracking.
* Tab drag-and-drop with edge auto-scrolling.
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

```java
TabPanePro tabPane = new TabPanePro();
var skin = (TabPaneProSkin) tabPane.getSkin();
var firstArea = skin.getTabHeaderFirstArea(); // CSS: .tab-pane-pro > .tab-header-area > .first-area {}
var stickyArea = skin.getTabHeaderStickyArea(); // CSS: .tab-pane-pro > .tab-header-area > .sticky-area {}
var lastArea = skin.getTabHeaderLastArea(); // CSS: .tab-pane-pro > .tab-header-area > .last-area {}
```
Note that these areas will rotate when the side of the pane is set to `RIGHT`, `BOTTOM`, or `LEFT`. However, the
library does not apply any automatic transformation or layout logic to adapt their content. This is intentional, as
there are multiple possible layout strategies, and enforcing a single approach could cause limitations or conflicts.
See `demo.css` for styling examples across different sides.

The standard `TabPane` hides its tab header area when there are no tabs. However, this behavior is not suitable if you
need to display the areas, which may contain various controls. To address this, use
`TabPaneProSkin#tabHeaderAreaPolicyProperty()`:

```java
skin.setTabHeaderAreaPolicy(TabHeaderAreaPolicy.ALWAYS_VISIBLE);
```

### Tabs Menu <a name="usage-tabs-menu"></a>

The Tabs Menu allows quick selection of a specific tab. This menu is typically used when not all tabs are visible to
the user. The library supports both custom menus and the standard menu. To show the standard menu, use the method
`TabPaneProSkin#showTabsMenu(Node)`. To determine whether this menu should be shown, use
`TabPaneProSkin#tabScrollBarNeededProperty()`.

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

```css
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

### Tab Drag and Drop <a name="usage-tab-drag-and-drop"></a>

TabPanePro supports drag-and-drop with automatic edge scrolling and flexible configuration options. Tabs can be dragged
either within a single TabPane or between different TabPane instances. Below is an example configuration for two
components: source and target. If the drag-and-drop operation occurs within the same TabPane, apply both source and
target settings to the same instance.

To enable drag and drop, follow these steps:

1. Set a shared context for all TabPane instances involved in the operation:

```java
var context = new DragAndDropContext();
sourceTabPane.setDragAndDropContext(context);
targetTabPane.setDragAndDropContext(context);
```

2. Configure the source TabPane:

```java
sourceTabPane.setTabDragEnabled(true);
// Optionally, restrict which tabs can be dragged using a predicate:
sourceTabPane.setTabDragPredicate(...);
// Optionally, provide a handler that's called when dragging starts:
sourceTabPane.setTabDragHandler(...);
// Provide the view of the tab being dragged — it will be shown in a popup:
var sourceSkin = (TabPaneProSkin) sourceTabPane.getSkin();
sourceSkin.setTabDragContentFactory(...);
// Set the cursor for the drag operation:
sourceSkin.setTabDragCursor(...);
```

3. Configure the target TabPane:

```java
targetTabPane.setTabDropEnabled(true);
// Optionally, restrict which tabs can be dropped using a predicate:
targetTabPane.setTabDropPredicate(...);
// Optionally, provide a handler that's called when the drag operation ends:
targetTabPane.setTabDropHandler(...);
// Set the scroll step for edge auto-scrolling:
var targetSkin = (TabPaneProSkin) targetTabPane.getSkin();
targetSkin.setTabDragScrollStep(10);
// Configure the drop position area either via CSS or in code.
// It's recommended to use an even width for the drop area, as its position is calculated as (width / 2).
// You can style the drop position in various ways, including adding nodes with arrow icons, etc.
StackPane dropPosition = targetSkin.getTabDropPosition(); // CSS: .tab-pane-pro > .tab-header-area > .drop-position {}
```

For a complete example and visual demonstration of these features, see the demo application included with the library.

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

