#  Triangle Rasterization Engine

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21+-blue.svg)](https://openjfx.io/)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

> Современная реализация алгоритмов растеризации треугольников с поддержкой различных режимов заливки

##  Описание

Triangle Rasterization Engine — это интерактивное приложение на JavaFX для визуализации и изучения алгоритмов растеризации треугольников. Проект демонстрирует классические методы компьютерной графики с использованием барицентрических координат и пиксельно-точной отрисовки.

###  Основные возможности

- **Интерактивное рисование**: Указывайте вершины треугольника кликами мыши
- **Режим однородной заливки**: Классическая заливка одним цветом
- **Режим градиентной заливки**: Плавная интерполяция цветов между тремя вершинами
- **Визуализация в реальном времени**: Мгновенная отрисовка результатов
- **Гибкая настройка цветов**: Палитра цветов для каждой вершины
- **Чистая архитектура**: Разделение логики, представления и управления

##  Быстрый старт

### Требования

- Java 17 или выше
- JavaFX 21+
- Maven (опционально)

### Установка и запуск

```bash
# Клонируйте репозиторий
git clone <repository-url>
cd CGVSU-task2

# Компиляция проекта
javac -d out src/main/java/**/*.java

# Запуск приложения
java -cp out Main
```

##  Использование

1. **Выберите режим заливки**:
   - `Однородная` — треугольник будет залит одним цветом
   - `Градиентная` — цвет будет интерполироваться между вершинами

2. **Настройте цвета**:
   - Для однородной заливки выберите один цвет
   - Для градиентной заливки задайте цвета для каждой из трех вершин

3. **Укажите вершины треугольника**:
   - Кликните три раза по холсту, чтобы задать координаты вершин
   - Визуальные маркеры покажут выбранные точки

4. **Нажмите "Нарисовать"**:
   - Треугольник будет растеризован с использованием выбранного алгоритма

5. **Очистка холста**:
   - Кнопка "Очистить" сбросит все параметры и очистит холст

## Архитектура

Проект построен на основе паттерна MVC с дополнительными слоями сервисов и фабрик.

```
src/main/java/
├── Main.java                           # Точка входа
├── controller/
│   ├── Launcher.java                   # Запуск JavaFX приложения
│   ├── MainViewController.java         # Контроллер главного окна
│   └── service/
│       ├── CanvasRenderer.java         # Отрисовка базовых элементов
│       ├── ColorManager.java           # Управление цветовыми схемами
│       ├── TriangleFiller.java         # Координация заливки треугольников
│       └── VertexManager.java          # Управление вершинами
├── model/
│   ├── AlgorithmConstants.java         # Константы алгоритмов
│   ├── FillMode.java                   # Enum режимов заливки
│   ├── Rasterization.java              # Интерфейс растеризации
│   ├── RasterizationAlgorithm.java     # Базовый абстрактный класс
│   ├── SolidFillAlgorithm.java         # Алгоритм однородной заливки
│   ├── GradientFillAlgorithm.java      # Алгоритм градиентной заливки
│   └── factory/
│       ├── FillAlgorithmFactory.java   # Интерфейс фабрики
│       └── StandardFillAlgorithmFactory.java
├── utils/
│   └── ConversionUtils.java            # Утилиты конвертации
└── resources/
    └── view/
        ├── mainwindow.fxml             # Разметка UI
        └── style.css                    # Стили интерфейса
```

### Ключевые компоненты

#### Алгоритмы растеризации

**`RasterizationAlgorithm`** — базовый класс, реализующий:
- Вычисление ограничивающего прямоугольника (bounding box)
- Расчет барицентрических координат
- Проверку принадлежности точки треугольнику
- Пиксельную итерацию с центрированием (x + 0.5, y + 0.5)

**`SolidFillAlgorithm`** — однородная заливка:
```java
protected Color computePixelColor(double w1, double w2, double w3) {
    return baseColor;  // Один цвет для всего треугольника
}
```

**`GradientFillAlgorithm`** — градиентная заливка:
```java
protected Color computePixelColor(double w1, double w2, double w3) {
    int r = (int) Math.round(w1 * color1.getRed() + w2 * color2.getRed() + w3 * color3.getRed());
    int g = (int) Math.round(w1 * color1.getGreen() + w2 * color2.getGreen() + w3 * color3.getGreen());
    int b = (int) Math.round(w1 * color1.getBlue() + w2 * color2.getBlue() + w3 * color3.getBlue());
    return new Color(clamp(r), clamp(g), clamp(b));
}
```

#### Паттерн Factory

**`FillAlgorithmFactory`** создает соответствующие алгоритмы на основе выбранного режима:
```java
public interface FillAlgorithmFactory {
    Rasterization createFillAlgorithm(FillMode mode, Color[] colors, Point[] vertices, int w, int h);
}
```

#### Сервисы

- **`VertexManager`**: Управляет состоянием вершин, проверяет их количество
- **`ColorManager`**: Управляет видимостью и значениями цветовых пикеров
- **`CanvasRenderer`**: Отвечает за отрисовку маркеров и очистку холста
- **`TriangleFiller`**: Координирует процесс заливки треугольника

## Математические основы

### Барицентрические координаты

Для точки P внутри треугольника ABC вычисляются веса (w₁, w₂, w₃):

$$
w_1 = \frac{Area(P, B, C)}{Area(A, B, C)}
$$

$$
w_2 = \frac{Area(A, P, C)}{Area(A, B, C)}
$$

$$
w_3 = \frac{Area(A, B, P)}{Area(A, B, C)}
$$

где $w_1 + w_2 + w_3 = 1$

### Площадь треугольника

Используется ориентированная площадь через векторное произведение:

$$
Area = \frac{1}{2} \left| (x_2 - x_1)(y_3 - y_1) - (x_3 - x_1)(y_2 - y_1) \right|
$$

### Интерполяция цвета

Цвет пикселя в градиентном режиме:

$$
C_{pixel} = w_1 \cdot C_1 + w_2 \cdot C_2 + w_3 \cdot C_3
$$

## Тестирование

Проект включает unit-тесты для проверки корректности работы:

```bash
# Запуск тестов
java -cp out:junit.jar org.junit.runner.JUnitCore controller.service.VertexManagerTest
java -cp out:junit.jar org.junit.runner.JUnitCore model.ClassicRasterizationAlgorithmTest
java -cp out:junit.jar org.junit.runner.JUnitCore model.InterpolatedRasterizationAlgorithmTest
```

### Покрытие тестами

- Управление вершинами (`VertexManagerTest`)
- Классический алгоритм растеризации
- Интерполированный алгоритм растеризации

## Образовательная ценность

Проект демонстрирует:

1. **Компьютерную графику**:
   - Алгоритмы растеризации
   - Барицентрические координаты
   - Интерполяция цветов
   - Пиксельная точность

2. **Паттерны проектирования**:
   - MVC (Model-View-Controller)
   - Factory Pattern
   - Strategy Pattern (через полиморфизм алгоритмов)
   - Service Layer

3. **Принципы разработки**:
   - SOLID principles
   - Clean Architecture
   - Separation of Concerns
   - Interface Segregation

## Производительность

- **Оптимизация**: Использование bounding box для минимизации проверок
- **Точность**: Центрирование пикселей (x + 0.5, y + 0.5)
- **Устойчивость**: Использование epsilon для проверки принадлежности

## Лицензия

Этот проект создан в образовательных целях в рамках курса компьютерной графики.

## Автор

Разработано с ❤️ для изучения основ компьютерной графики

## Благодарности

Проект основан на классических алгоритмах компьютерной графики и лучших практиках разработки на Java.

---

**⭐ Если проект был полезен, не забудьте поставить звезду!**
