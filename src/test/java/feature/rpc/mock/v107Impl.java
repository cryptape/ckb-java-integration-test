package feature.rpc.mock;

import com.ckb.base.BeforeSuite;
import lombok.Data;
import org.nervos.ckb.type.TransactionProof;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class v107Impl extends BeforeSuite implements v107{
    @Override
    public Map<String, Object> get_transaction_and_witness_proof(ArrayList<String> tx_hashes, String block_hash) {
        Map<String, Object> requestBody = getBodyForJsonRpc();
        requestBody.put("method", "get_transaction_and_witness_proof");
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(tx_hashes);
        arrayList.add(block_hash);
        requestBody.put("params", arrayList);
        return requestBody;
    }

    public class TransactionAndWitnessProof {
        public MerkleProof transactions_proof;
        public MerkleProof witnesses_proof;
        public String block_hash;
        @Data
        public class MerkleProof {
            public List<Integer> indices;
            public List<byte[]> lemmas;
        }
    }

    @Override
    public Map<String, Object> verify_transaction_and_witness_proof(TransactionAndWitnessProof tx_proof) {
        Map<String, Object> requestBody = getBodyForJsonRpc();
        requestBody.put("method", "verify_transaction_and_witness_proof");
        requestBody.put("params", tx_proof);
        return requestBody;
    }

    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("0x749612a4c3b47d6046406826ed865bafefbfdff7314cb71346599322aafc178a");
        System.out.println(new v107Impl().get_transaction_and_witness_proof(arrayList, null));
    }
}
