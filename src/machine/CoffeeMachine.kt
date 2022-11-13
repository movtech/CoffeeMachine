package machine

var quantityOfWaterAvailable = 400 // ml
var quantityOfMilkAvailable = 540 // ml
var quantityOfCoffeeBeansAvailable = 120 // g
var quantityOfDisposableCupsAvailable = 9 // pc
var cashInDrawerAmount = 550 // $

val materialsNotAvailable = mutableListOf<String>()

fun main(args: Array<String>) {
    while (true) {
        println("Write action (buy, fill, take, remaining, exit):")

        val userInput = readLine()!!
        val coffeeMachine = CoffeeMachine()

        if (userInput == "exit") break else coffeeMachine.readUserInput(userInput)
    }
}

enum class CoffeeMaterials(val water: Int, val milk: Int, val coffeeBeans: Int, val unitPrice: Int) {
    ESPRESSO(250, 0, 16, 4),
    LATTE(350,75,20,7),
    CAPPUCCINO(200,100,12,6)
}

class CoffeeMachine {

    fun readUserInput(input: String) {
        when (input) {
            "buy" -> buy()
            "fill" -> fill()
            "take" -> take()
            "remaining" -> printAvailableStock()
            else -> println("Invalid input")
        }
    }

    fun buy() {
        println()
        try {
            println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:")
            val item = readLine()!!

            when (item) {
                "1" -> calculateEspressoCostAndMaterialAavailable()
                "2" -> calculateLatteCostAndMaterialsAvailable()
                "3" -> calculateCappuccinoCostAndMaterialsAvailable()
                "back" -> {
                    println()
                    return
                }
                else -> println("Invalid input")
            }
        } catch (e: NumberFormatException) {
            println("Invalid input")
        }
    }

    fun fill() {
        println()

        try {
            println("Write how many ml of water you want to add:")
            quantityOfWaterAvailable += readLine()!!.toInt()

            println("Write how many ml of milk you want to add: ")
            quantityOfMilkAvailable += readLine()!!.toInt()

            println("Write how many grams of coffee beans you want to add: ")
            quantityOfCoffeeBeansAvailable += readLine()!!.toInt()

            println("Write how many disposable cups you want to add:")
            quantityOfDisposableCupsAvailable += readLine()!!.toInt()

            println()
        } catch (e: NumberFormatException) {
            println("Invalid input")
        }
    }

    fun take() {
        println()

        val cashOutAmount = cashInDrawerAmount
        cashInDrawerAmount -= cashInDrawerAmount

        println("I gave you $$cashOutAmount")

        println()
    }

    fun calculateEspressoCostAndMaterialAavailable() {
        val quantityOfCoffeeRequired = 1

        // Calculate number of espresso available materials can make
        val numberOfCoffeeAvailableWaterCanMake = quantityOfWaterAvailable / CoffeeMaterials.ESPRESSO.water
        val numberOfCoffeeDisposableCupsCanMake = quantityOfDisposableCupsAvailable / quantityOfCoffeeRequired
        val numberOfCoffeeAvailableCoffeeBeansCanMake = quantityOfCoffeeBeansAvailable / CoffeeMaterials.ESPRESSO.coffeeBeans

        val productionMix = mutableListOf(numberOfCoffeeAvailableWaterCanMake, numberOfCoffeeDisposableCupsCanMake, numberOfCoffeeAvailableCoffeeBeansCanMake)

        var minCoffeeProductionQuantity = productionMix.first()

        for (number in productionMix) {
            if (minCoffeeProductionQuantity > number) minCoffeeProductionQuantity = number
        }

        when {
            quantityOfCoffeeRequired > minCoffeeProductionQuantity -> {
                // One or more material is not sufficient, print the insufficient material
                if (quantityOfWaterAvailable < quantityOfCoffeeRequired * CoffeeMaterials.ESPRESSO.water) {
                    materialsNotAvailable.add("water")
                }

                if (quantityOfDisposableCupsAvailable < quantityOfCoffeeRequired * quantityOfCoffeeRequired) {
                    materialsNotAvailable.add("disposable cups")
                }

                if (quantityOfCoffeeBeansAvailable < quantityOfCoffeeRequired * CoffeeMaterials.ESPRESSO.coffeeBeans) {
                    materialsNotAvailable.add("coffee beans")
                }

                val materialsNotAvailableSize = materialsNotAvailable.size

                when (materialsNotAvailableSize) {
                    1 -> printMessage(false, materialsNotAvailable[0])
                    2 -> printMessage(false, materialsNotAvailable[0] + ", " + materialsNotAvailable[1])
                    3 -> printMessage(false, materialsNotAvailable[0] + ", " + materialsNotAvailable[1] + ", " + materialsNotAvailable[2])
                }
            }
            quantityOfCoffeeRequired < minCoffeeProductionQuantity || quantityOfCoffeeRequired == minCoffeeProductionQuantity -> {
                // Materials available are sufficient, print the print message
                printMessage(isMaterialSufficient = true)

                // Deduct quantity of espresso purchased from available product mix
                quantityOfWaterAvailable -= quantityOfCoffeeRequired * CoffeeMaterials.ESPRESSO.water
                quantityOfDisposableCupsAvailable -= quantityOfCoffeeRequired * quantityOfCoffeeRequired
                quantityOfCoffeeBeansAvailable -= quantityOfCoffeeRequired * CoffeeMaterials.ESPRESSO.coffeeBeans

                // Add to cash in drawer
                cashInDrawerAmount += quantityOfCoffeeRequired * CoffeeMaterials.ESPRESSO.unitPrice
            }
        }

        println()
    }

    fun calculateLatteCostAndMaterialsAvailable() {
        val quantityOfCoffeeRequired = 1

        // Calculate number of latte available materials can make
        val numberOfCoffeeAvailableWaterCanMake = quantityOfWaterAvailable / CoffeeMaterials.LATTE.water
        val numberOfCoffeeAvailableMilkCanMake = quantityOfMilkAvailable / CoffeeMaterials.LATTE.milk
        val numberOfCoffeeDisposableCupsCanMake = quantityOfDisposableCupsAvailable / quantityOfCoffeeRequired
        val numberOfCoffeeAvailableCoffeeBeansCanMake = quantityOfCoffeeBeansAvailable / CoffeeMaterials.LATTE.coffeeBeans

        val productionMix = mutableListOf(numberOfCoffeeAvailableWaterCanMake, numberOfCoffeeAvailableMilkCanMake, numberOfCoffeeDisposableCupsCanMake, numberOfCoffeeAvailableCoffeeBeansCanMake)

        var minCoffeeProductionQuantity = productionMix.first()

        for (number in productionMix) {
            if (minCoffeeProductionQuantity > number) minCoffeeProductionQuantity = number
        }

        when {
            quantityOfCoffeeRequired > minCoffeeProductionQuantity -> {
                // One or more material is not sufficient, print the insufficient material
                if (quantityOfWaterAvailable < quantityOfCoffeeRequired * CoffeeMaterials.LATTE.water) {
                    materialsNotAvailable.add("water")
                }

                if (quantityOfMilkAvailable < quantityOfCoffeeRequired * CoffeeMaterials.LATTE.milk) {
                    materialsNotAvailable.add("water")
                }

                if (quantityOfDisposableCupsAvailable < quantityOfCoffeeRequired * quantityOfCoffeeRequired) {
                    materialsNotAvailable.add("disposable cups")
                }

                if (quantityOfCoffeeBeansAvailable < quantityOfCoffeeRequired * CoffeeMaterials.LATTE.coffeeBeans) {
                    materialsNotAvailable.add("coffee beans")
                }

                val materialsNotAvailableSize = materialsNotAvailable.size

                when (materialsNotAvailableSize) {
                    1 -> printMessage(false, materialsNotAvailable[0])
                    2 -> printMessage(false, materialsNotAvailable[0] + ", " + materialsNotAvailable[1])
                    3 -> printMessage(false, materialsNotAvailable[0] + ", " + materialsNotAvailable[1] + ", " + materialsNotAvailable[2])
                    4 -> printMessage(false, materialsNotAvailable[0] + ", " + materialsNotAvailable[1] + ", " + materialsNotAvailable[2] + ", " + materialsNotAvailable[3])
                }
            }
            quantityOfCoffeeRequired < minCoffeeProductionQuantity || quantityOfCoffeeRequired == minCoffeeProductionQuantity -> {
                // Materials available are sufficient, print the print message
                printMessage(isMaterialSufficient = true)

                // Deduct quantity of latte purchased from available product mix
                quantityOfWaterAvailable -= quantityOfCoffeeRequired * CoffeeMaterials.LATTE.water
                quantityOfMilkAvailable -= quantityOfCoffeeRequired * CoffeeMaterials.LATTE.milk
                quantityOfDisposableCupsAvailable -= quantityOfCoffeeRequired * quantityOfCoffeeRequired
                quantityOfCoffeeBeansAvailable -= quantityOfCoffeeRequired * CoffeeMaterials.LATTE.coffeeBeans

                // Add to cash in drawer
                cashInDrawerAmount += quantityOfCoffeeRequired * CoffeeMaterials.LATTE.unitPrice
            }
        }

        println()
    }

    fun calculateCappuccinoCostAndMaterialsAvailable() {
        val quantityOfCoffeeRequired = 1

        // Calculate number of cappuccino available materials can make
        val numberOfCoffeeAvailableWaterCanMake = quantityOfWaterAvailable / CoffeeMaterials.CAPPUCCINO.water
        val numberOfCoffeeAvailableMilkCanMake = quantityOfMilkAvailable / CoffeeMaterials.CAPPUCCINO.milk
        val numberOfCoffeeDisposableCupsCanMake = quantityOfDisposableCupsAvailable / quantityOfCoffeeRequired
        val numberOfCoffeeAvailableCoffeeBeansCanMake = quantityOfCoffeeBeansAvailable / CoffeeMaterials.CAPPUCCINO.coffeeBeans

        val productionMix = mutableListOf(numberOfCoffeeAvailableWaterCanMake, numberOfCoffeeAvailableMilkCanMake, numberOfCoffeeDisposableCupsCanMake, numberOfCoffeeAvailableCoffeeBeansCanMake)

        var minCoffeeProductionQuantity = productionMix.first()

        for (number in productionMix) {
            if (minCoffeeProductionQuantity > number) minCoffeeProductionQuantity = number
        }

        when {
            quantityOfCoffeeRequired > minCoffeeProductionQuantity -> {
                // One or more material is not sufficient, print the insufficient material
                if (quantityOfWaterAvailable < quantityOfCoffeeRequired * CoffeeMaterials.CAPPUCCINO.water) {
                    materialsNotAvailable.add("water")
                }

                if (quantityOfMilkAvailable < quantityOfCoffeeRequired * CoffeeMaterials.CAPPUCCINO.milk) {
                    materialsNotAvailable.add("water")
                }

                if (quantityOfDisposableCupsAvailable < quantityOfCoffeeRequired * quantityOfCoffeeRequired) {
                    materialsNotAvailable.add("disposable cups")
                }

                if (quantityOfCoffeeBeansAvailable < quantityOfCoffeeRequired * CoffeeMaterials.CAPPUCCINO.coffeeBeans) {
                    materialsNotAvailable.add("coffee beans")
                }

                val materialsNotAvailableSize = materialsNotAvailable.size

                when (materialsNotAvailableSize) {
                    1 -> printMessage(false, materialsNotAvailable[0])
                    2 -> printMessage(false, materialsNotAvailable[0] + ", " + materialsNotAvailable[1])
                    3 -> printMessage(false, materialsNotAvailable[0] + ", " + materialsNotAvailable[1] + ", " + materialsNotAvailable[2])
                    4 -> printMessage(false, materialsNotAvailable[0] + ", " + materialsNotAvailable[1] + ", " + materialsNotAvailable[2] + ", " + materialsNotAvailable[3])
                }
            }
            quantityOfCoffeeRequired < minCoffeeProductionQuantity || quantityOfCoffeeRequired == minCoffeeProductionQuantity -> {
                // Materials available are sufficient, print the print message
                printMessage(isMaterialSufficient = true)

                // Deduct quantity of cappuccino purchased from available product mix
                quantityOfWaterAvailable -= quantityOfCoffeeRequired * CoffeeMaterials.CAPPUCCINO.water
                quantityOfMilkAvailable -= quantityOfCoffeeRequired * CoffeeMaterials.CAPPUCCINO.milk
                quantityOfDisposableCupsAvailable -= quantityOfCoffeeRequired * quantityOfCoffeeRequired
                quantityOfCoffeeBeansAvailable -= quantityOfCoffeeRequired * CoffeeMaterials.CAPPUCCINO.coffeeBeans

                // Add to cash in drawer
                cashInDrawerAmount += quantityOfCoffeeRequired * CoffeeMaterials.CAPPUCCINO.unitPrice
            }
        }

        println()
    }

    fun printAvailableStock() {
        println()

        val availableStock = """
        The coffee machine has:
        $quantityOfWaterAvailable ml of water
        $quantityOfMilkAvailable ml of milk
        $quantityOfCoffeeBeansAvailable g of coffee beans
        $quantityOfDisposableCupsAvailable disposable cups
        $$cashInDrawerAmount of money
    """.trimIndent()

        println(availableStock)
        println()
    }

    fun printMessage(isMaterialSufficient: Boolean, material: String = "") {
        if (isMaterialSufficient) println("I have enough resources, making you a coffee!") else println("Sorry, not enough $material!")
    }
}