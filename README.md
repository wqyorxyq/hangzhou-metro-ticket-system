# Hangzhou Metro Ticketing Simulator

A Java Swing coursework project that simulates a self-service metro ticket machine. It combines object-oriented station and line models, interactive line diagrams, and an event-driven ticket purchase interface.

The interface is in Chinese. This is an educational desktop simulation using a small hard-coded sample network, with no connection to a live transit or payment service.

![The ticketing simulator with a selected journey and simulated payment](docs/preview.png)

## Features

- Browse sample data for Lines 1, 2, 4, and 5, with color-coded diagrams and transfer-station markers.
- Select an origin and destination using dropdowns or station buttons.
- Display an estimated fare, the amount paid, and the remaining balance.
- Simulate coin and banknote payments, generate an on-screen ticket, and calculate change.
- Handle incomplete selections, insufficient payment, repeated ticket issuance, and transaction reset.

## Run

Requires a JDK version 17 or later and a graphical desktop. There are no third-party dependencies. Chinese fonts are recommended; the source requests Microsoft YaHei and SimSun, with platform fallback where unavailable.

From the repository root:

```sh
mkdir out
javac -encoding UTF-8 --release 17 -d out src/HangzhouMetroTicketSystem.java
java -cp out HangzhouMetroTicketSystem
```

Or use the included launcher:

```powershell
# Windows PowerShell
powershell -ExecutionPolicy Bypass -File .\run.ps1
```

```sh
# macOS or Linux with a graphical desktop
sh run.sh
```

## Try a transaction

1. Choose `1号线 - 湘湖` as the origin and `1号线 - 滨康路` as the destination.
2. The demonstration fare is 2.0 yuan. Click the 5.0-yuan banknote button.
3. Click `出票` to display the ticket and 3.0 yuan in simulated change.
4. Click `重置购票` to start another transaction.

## Implementation

| Component | Role |
| --- | --- |
| `MetroStation` | Station name, line, ID, and transfer marker |
| `MetroLine` | Line metadata and an ordered collection of stations |
| `LineDisplayPanel` | Line navigation, custom Java2D diagrams, and station selection |
| `MetroStationClickListener` | Callback connecting station buttons to the selection form |
| `HangzhouMetroTicketSystem` | Main Swing window, fare display, payment state, and ticket output |

All classes are kept in `src/HangzhouMetroTicketSystem.java`, preserving the original single-file coursework structure.

The publication version adjusts button text colors and navigation symbols for readability with the Windows look and feel. The original fare and transaction logic is retained.

## Validation

Compiled on Windows with JDK 25 using `javac --release 17`. A local Swing smoke check verified the 57 sample station entries, empty and same-station selections, an adjacent-station fare, payment balance updates, the ticket-ready prompt, and reset behavior. Full end-to-end manual validation of every dialog and cross-platform rendering has not been performed.

## Scope and limitations

- Fare estimates use the absolute difference between station IDs, a simplified tiered formula, and an 8-yuan cap. Station IDs are **not** geographic distance, especially across lines. The program does not compute a valid transfer route or reproduce official fares.
- The sample network is incomplete and is not maintained as current Hangzhou Metro data. Transfer markers are display metadata, not a routing graph.
- Payments, change, and ticket printing are simulated on screen. There is no payment gateway, physical ticket printer, persistent transaction store, or real-money handling.
- This project demonstrates Java, Swing, object-oriented modeling, GUI interaction, and transaction-state handling. It does not implement computer vision, machine learning, or a world model.

## 中文说明

杭州地铁自助购票模拟器，Java 课程设计项目。支持线路浏览、站点选择、演示票价计算、模拟投币与付钞、屏幕出票、找零和购票重置。

使用 JDK 17 或以上版本运行，无第三方依赖。站点数据为硬编码教学示例，票价按站点编号差估算，不能用于实际出行计费或换乘规划。运行方式见上文。
