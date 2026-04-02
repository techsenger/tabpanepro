# Techsenger TabPanePro

Techsenger TabPanePro is a lightweight JavaFX library that extends the standard TabPane with practical features such
as custom control areas, custom tab shapes, tab drag-and-drop with edge scrolling, a tab scrollbar, and more. The
library is built on top of the standard TabPaneSkin, carefully extracted from the OpenJFX project along with a
minimal set of required classes.

## Table of Contents
* [Demo](#demo)
* [Features](#features)
* [Dependencies](#dependencies)
* [Usage](#usage)
    * [Control Areas](#usage-control-areas)
    * [Tab Custom Shape](#usage-tab-custom-shape)
    * [Tabs Menu](#usage-tabs-menu)
    * [Tabs ScrollBar](#usage-tabs-scroll-bar)
    * [Tab Scrolling](#usage-tab-scrolling)
    * [Tab Drag and Drop](#usage-tab-drag-and-drop)
* [Code building](#code-building)
* [Running Demo](#running-demo)
* [License](#license)
* [Contributing](#contributing)
* [Support Us](#support-us)

## Demo <a name="demo"></a>

![tabpanepro-demo0](https://github.com/user-attachments/assets/809d0779-6c9d-4d28-9a9a-e126739d5955)

![tabpanepro-demo1](https://github.com/user-attachments/assets/890f7504-2ad1-4b1b-92e4-60ef26a5ac68)

## Features <a name="features"></a>

Key features include:

* Two additional areas placed before and after all tabs, available for any custom purpose.
* A sticky area, located between the tabs and the trailing area, typically used for a New Tab button.
* Support for custom tab shapes with control over view order and gaps between tabs.
* Ability to show the tab header area even when there are no tabs.
* Support for both the standard and custom tab menus.
* Supports tab scrolling via a ScrollBar, with four CSS-configurable positions per side.
* API for programmatic tab scrolling with scroll state tracking.
* Tab drag-and-drop with edge auto-scrolling.
* A demo application showcasing all library features.
* Comprehensive documentation.

## Dependencies <a name="dependencies"></a>

This project is available on Maven Central:

```
<dependency>
    <groupId>com.techsenger.tabpanepro</groupId>
    <artifactId>tabpanepro-core</artifactId>
    <version>${tabpanepro.version}</version>
</dependency>
```

## Usage <a name="usage"></a>

### Control Areas <a name="usage-control-areas"></a>

The library provides three optional areas that can be used if needed. You can access these areas through the skin:

```java
TabPanePro tabPane = new TabPanePro();
TabPaneProSkin skin = (TabPaneProSkin) tabPane.getSkin();
TabHeaderArea tabHeaderArea = skin.getTabHeaderArea();
StackPane firstArea = tabHeaderArea.getFirstArea(); // CSS: .tab-pane-pro > .tab-header-area > .first-area {}
StackPane stickyArea = tabHeaderArea.getStickyArea(); // CSS: .tab-pane-pro > .tab-header-area > .sticky-area {}
StackPane lastArea = tabHeaderArea.getLastArea(); // CSS: .tab-pane-pro > .tab-header-area > .last-area {}
```
Note that these areas will rotate when the side of the pane is set to `RIGHT`, `BOTTOM`, or `LEFT`. However, the
library does not apply any automatic transformation or layout logic to adapt their content. This is intentional, as
there are multiple possible layout strategies, and enforcing a single approach could cause limitations or conflicts.
See `demo.css` for styling examples across different sides.

The standard `TabPane` hides its tab header area when there are no tabs. However, this behavior is not suitable if you
need to display the areas, which may contain various controls. To address this, use
`TabHeaderArea#policyProperty()`:

```java
tabHeaderArea.setPolicy(TabHeaderAreaPolicy.ALWAYS_VISIBLE);
```

### Tab Custom Shape <a name="usage-tab-custom-shape"></a>

The library allows you to create virtually any custom shape for tabs. To achieve this, the following properties are provided:

* `TabHeaderArea#tabHeaderFactoryProperty()` — holds the factory used to create custom `TabHeaderSkin` instances.
Although this class is public, its implementation is mostly hidden. Developers are given access only to the
`TabHeaderContext`, which is sufficient for adding new nodes and overriding `layoutChildren()`.
A key method here is `TabHeaderSkin#layoutChildren(Insets)`, which allows you to use the default layout for child
nodes with additional padding. For example, if you want to create a custom shape with a 5-pixel gap between it and
`.tab-container`, you can call `skin.layoutChildren(new Insets(5));`. This will add the necessary spacing without
having to rewrite CSS styles.
* `TabHeaderArea#tabGapProperty()` — defines the spacing between tab headers. If the value is negative, the headers
will overlap each other.
* `TabHeaderArea#tabViewOrderResolverProperty()` — holds a resolver that defines the view order for each `TabHeaderSkin`.

### Tabs Menu <a name="usage-tabs-menu"></a>

The Tabs Menu allows quick selection of a specific tab. This menu is typically used when not all tabs are visible to
the user. The library supports both custom menus and the standard menu. To show the standard menu, use the method
`TabHeaderArea#showTabsMenu(Node)`. To determine whether this menu should be shown, use
`TabHeaderArea#scrollBarNeededProperty()`.

### Tabs ScrollBar <a name="usage-tabs-scroll-bar"></a>

To enable scrolling of tabs using a `ScrollBar`, first set the `TabHeaderArea#scrollBarEnabledProperty()` property
to `true`. As a result, the `ScrollBar` will be added to the tab header area, but it will remain invisible by
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
To scroll the tabs, use the method `TabHeaderArea#scrollTabHeadersBy(double)`. To determine the scroll state, use the
three read-only properties: `TabHeaderArea#headersRegionWidthProperty()`, `TabHeaderArea#headersRegionOffsetProperty()`,
and `TabHeaderArea#headersClipWidthProperty()`. See the demo for an example implementation.

### Tab Drag and Drop <a name="usage-tab-drag-and-drop"></a>

TabPanePro supports drag-and-drop with automatic edge scrolling and flexible configuration options. Tabs can be dragged
either within a single TabPane or between different TabPane instances. Below is an example configuration for two
components: source and target. If the drag-and-drop operation occurs within the same TabPane, apply both source and
target settings to the same instance.

To enable drag and drop, follow these steps:

1. Set a shared context for all TabPane instances involved in the operation:

```java
DragAndDropContext context = new DragAndDropContext();
sourceTabPane.setDragAndDropContext(context);
targetTabPane.setDragAndDropContext(context);
```

2. Configure the source TabPane:

```java
sourceTabPane.setTabDragEnabled(true);
// Optionally, restrict which tabs can be dragged using a filter:
sourceTabPane.setTabDragFilter(...);
// Optionally, provide a handler that's called when dragging starts:
sourceTabPane.addTabDragHandler(...);
// Optionally, provide a handler that's called when the drag operation ends:
sourceTabPane.addTabDropHandler(...);
// Retrieve the TabHeaderArea from the source skin
TabPaneProSkin sourceSkin = (TabPaneProSkin) sourceTabPane.getSkin();
TabHeaderArea sourceTabHeaderArea = sourceSkin.getTabHeaderArea();
// Provide the view of the tab being dragged — it will be shown in a popup:
sourceTabHeaderArea.setTabDragContentFactory(...);
// Set the cursor for the drag operation:
sourceTabHeaderArea.setTabDragCursor(...);
```

3. Configure the target TabPane:

```java
targetTabPane.setTabDropEnabled(true);
// Optionally, restrict which tabs can be dropped using a filter:
targetTabPane.setTabDropFilter(...);
// Retrieve the TabHeaderArea from the target skin
TabPaneProSkin targetSkin = (TabPaneProSkin) targetTabPane.getSkin();
TabHeaderArea targetTabHeaderArea = targetSkin.getTabHeaderArea();
// Set the scroll step for edge auto-scrolling:
targetTabHeaderArea.setTabDragScrollStep(...);
// Configure the drop position area either via CSS or in code.
// It's recommended to use an even width for the drop area, as its position is calculated as (width / 2).
// You can style the drop position in various ways, including adding nodes with arrow icons, etc.
TabDropPosition dropPosition = targetSkin.getTabDropPosition(); // CSS: .tab-pane-pro > .tab-header-area > .tab-drop-position {}
// If you use custom tab shapes, you may need to adjust the drop position
dropPosition.setOffset(...);
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

## Support Us <a name="support-us"></a>

You can support our open-source work through [GitHub Sponsors](https://github.com/sponsors/techsenger).
Your contribution helps us maintain projects, develop new features, and provide ongoing improvements.
Multiple sponsorship tiers are available, each offering different levels of recognition and benefits.

