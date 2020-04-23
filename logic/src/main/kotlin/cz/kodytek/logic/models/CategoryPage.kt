package cz.kodytek.logic.models

data class CategoryPage(
        val category: Category,
        val currentPage: Int,
        val pagesCount: Int,
        val products: List<Product>
)