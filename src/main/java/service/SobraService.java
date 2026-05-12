package service;

import model.Sobra;
import repository.SobraRepository;

import java.util.List;

public class SobraService {
    private final SobraRepository sobraRepository = new SobraRepository();

    public List<Sobra> pesquisar(String termo) {
        return sobraRepository.findAll(termo);
    }

    public void salvar(Sobra sobra) {
        if (sobra.getProdutoId() <= 0) {
            throw new IllegalArgumentException("Selecione o produto da sobra.");
        }
        if (sobra.getTamanhoOriginal() <= 0 || sobra.getTamanhoSobra() <= 0) {
            throw new IllegalArgumentException("Informe tamanhos validos.");
        }
        if (sobra.getTamanhoSobra() > sobra.getTamanhoOriginal()) {
            throw new IllegalArgumentException("A sobra nao pode ser maior que a barra original.");
        }
        if (sobra.getLocalizacao() == null || sobra.getLocalizacao().isBlank()) {
            throw new IllegalArgumentException("Informe a localizacao da sobra.");
        }
        if (sobra.getStatus() == null || sobra.getStatus().isBlank()) {
            sobra.setStatus("Disponivel");
        }
        sobraRepository.save(sobra);
    }

    public void reutilizar(Sobra sobra) {
        if (sobra == null) {
            throw new IllegalArgumentException("Selecione uma sobra.");
        }
        sobraRepository.updateStatus(sobra.getId(), "Reutilizada");
    }

    public void perda(Sobra sobra) {
        if (sobra == null) {
            throw new IllegalArgumentException("Selecione uma sobra.");
        }
        sobraRepository.updateStatus(sobra.getId(), "Perda");
    }

    public int disponiveis() {
        return sobraRepository.countDisponiveis();
    }
}
