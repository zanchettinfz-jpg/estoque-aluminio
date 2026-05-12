package util;

public class QRCodeUtil {

    private QRCodeUtil() {
    }

    public static String gerarPayloadProduto(String codigo) {
        return "produto:" + codigo;
    }

    public static void gerarQRCodeFuturo(String payload) {
        throw new UnsupportedOperationException("Modulo reservado para integracao futura com ZXing: " + payload);
    }
}
