package br.ufrn.poenalista.shoppinglist;

/**
 * Identifica a lista de compras em uso e quem está operando sobre ela.
 * Contém apenas dados simples (ids e nomes) para poder ser usado fora de transação.
 */
public record ShoppingListContext(Long shoppingListId, String shoppingListName, String houseName, Long userId) {
}

