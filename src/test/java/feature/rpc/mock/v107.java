package feature.rpc.mock;

import org.nervos.ckb.type.TransactionProof;
import java.util.ArrayList;
import java.util.Map;

public interface v107 {
    public Map<String, Object> get_transaction_and_witness_proof(ArrayList<String> tx_hashes, String block_hash);
    public Map<String, Object> verify_transaction_and_witness_proof(v107Impl.TransactionAndWitnessProof tx_proof);
}
