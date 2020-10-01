package ed2_atividade01;

import java.util.Set;

public class ArvoreAVLMap<K extends Comparable<K>, V> implements MyMap<K,V> {

    private No raiz;

    private class No {
        private K key;
        private V value;
        private int altura;
        private int size;
        private No direita;
        private No esquerda;

        public No(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return this.key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return this.value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public No getDireita() {
            return this.direita;
        }

        public void setDireita(No direita) {
            this.direita = direita;
        }

        public No getEsquerda() {
            return this.esquerda;
        }

        public void setEsquerda(No esquerda) {
            this.esquerda = esquerda;
        }

        public int getAltura() {
            return altura;
        }

        public void setAltura(int altura) {
            this.altura = altura;
        }

        public int getSize() {
            return this.size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int compareTo(K key) {
            return this.key.compareTo(key);
        }
    }

    @Override
    public int size() {
        return size(this.raiz);
    }

    private int size(No no) {
        if (no == null)
            return 0;

        return no.getSize();
    }

    @Override
    public boolean isEmpty() {
        return this.raiz == null;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        return get(key, this.raiz);
    }

    private V get(K key, No atual) {
        if (atual.compareTo(key) == 0) {
            return atual.value;
        }
        else {
            if (atual.compareTo(key) > 0) {
                if (atual.getEsquerda() == null)
                    return null;
                else
                    return get(key, atual.getEsquerda());
            }
            else {
                if (atual.getDireita() == null)
                    return null;
                else
                    return get(key, atual.getDireita());
            }
        }
    }

    @Override
    public void put(K key, V value) {
        this.raiz = put(key, this.raiz, value);
    }

    private No put(K key, No no, V value) {
        if (no == null)
            no = new No(key,value);

        else if (no.compareTo(key) == 0)
            no = new No(key, value);

        else if (no.compareTo(key) > 0)//esquerda
            no.setEsquerda(put(key, no.getEsquerda(), value));

        else if (no.compareTo(key) < 0)// direita
            no.setDireita(put(key, no.getDireita(), value));

        no.setSize(1 + size(no.getEsquerda()) + size(no.getDireita()));
        no.setAltura(1 + max(altura(no.getEsquerda()), altura(no.getDireita())));
        no = balanceamento(no);
        return no;
    }

    public int altura() {
        return altura(this.raiz);
    }

    private int altura(No no) {
        if (no == null)
            return -1;

        return no.getAltura();
    }

    private int max(int esquerda, int direita) {
        if (esquerda > direita)
            return esquerda;

        return direita;
    }

    private No rotacaoSimplesDireita(No no) {
        No aux = no.getEsquerda();
        no.setEsquerda(aux.getDireita());
        aux.setDireita(no);
        aux.setSize(no.getSize());
        no.setSize(1 + size(no.getEsquerda()) + size(no.getDireita()));
        no.setAltura(max(altura(no.getEsquerda()), altura(no.getDireita())) + 1);
        aux.setAltura(max(altura(aux.getEsquerda()), no.getAltura()) + 1);
        return aux;
    }

    private No rotacaoSimplesEsquerda(No no) {
        No aux = no.getDireita();
        no.setDireita(aux.getEsquerda());
        aux.setEsquerda(no);
        aux.setSize(no.getSize());
        no.setSize(1 + size(no.getEsquerda()) + size(no.getDireita()));
        no.setAltura(max(altura(no.getEsquerda()), altura(no.getDireita())) + 1);
        aux.setAltura(max(altura(aux.getDireita()), no.getAltura()) + 1);
        return aux;
    }

    private No rotacaoDuplaDireita(No no) {
        no.setEsquerda(rotacaoSimplesEsquerda(no.getEsquerda()));
        return rotacaoSimplesDireita(no);
    }

    private No rotacaoDuplaEsquerda(No no) {
        no.setDireita(rotacaoSimplesDireita(no.getDireita()));
        return rotacaoSimplesEsquerda(no);
    }

    private int fatorBalanceamento(No no) {
        return (altura(no.getEsquerda()) - altura(no.getDireita()));
    }

    private No balanceamento(No no) {
        if (fatorBalanceamento(no) == 2) {
            if (fatorBalanceamento(no.getEsquerda()) > 0)
                no = rotacaoSimplesDireita(no);
            else
                no = rotacaoDuplaDireita(no);
        }
        else if (fatorBalanceamento(no) == -2) {
            if (fatorBalanceamento(no.getDireita()) < 0)
                no = rotacaoSimplesEsquerda(no);
            else
                no = rotacaoDuplaEsquerda(no);
        }
        no.setAltura(max(altura(no.getEsquerda()), altura(no.getDireita())) + 1);
        return no;
    }

    @Override
    public void remove(K key) {
        this.raiz = remove(key, this.raiz);
    }

    private No remove(K key, No no) {
        if (key == null) {
            return null;
        }
        else if (no.compareTo(key) > 0) {
            no.setEsquerda(remove(key, no.getEsquerda()));
            return balanceamento(no);
        }
        else if (no.compareTo(key) < 0) {
            no.setDireita(remove(key, no.getDireita()));
            return balanceamento(no);
        }
        else {
            if (no.getEsquerda() == null && no.getDireita() == null) {
                return null;
            }
            if (no.getEsquerda() == null) {
                return no.getDireita();
            }
            if (no.getDireita() == null) {
                return no.getEsquerda();
            }
            if (no.getEsquerda() != null && no.getDireita() != null) {
                no.setKey(menorDireita(no.getDireita()).getKey());
                no.setDireita(remove(no.getKey(), no.getDireita()));
            }
            else {
                no = (no.getEsquerda() != null) ? no.getEsquerda() : no.getDireita();
            }
            return balanceamento(no);
        }
    }

    private No menorDireita(No no) {
        if (no.getEsquerda() == null) {
            return no;
        }
        else {
            no = no.getEsquerda();
            return menorDireita(no);
        }
    }

    @Override
    public void clear() {
        this.raiz = null;
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    public void emOrdem() {
        emOrdem(this.raiz);
    }

    private void emOrdem(No atual) {
        if (atual != null) {
            emOrdem(atual.getEsquerda());
            System.out.print(atual.getKey() + "    ");
            emOrdem(atual.getDireita());
        }
    }
}
