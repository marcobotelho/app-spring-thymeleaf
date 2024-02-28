package com.projeto.appspringthymeleaf.record;

import jakarta.validation.constraints.NotBlank;

public record RedefinirSenhaRecord(@NotBlank(message = "Token obrigatório") String token,
                @NotBlank(message = "Senha atual obrigatória") String senhaAtual,
                @NotBlank(message = "Senha nova obrigatória") String senhaNova,
                @NotBlank(message = "Confirmação de senha nova obrigatória") String senhaNovaConfirmacao) {

}
