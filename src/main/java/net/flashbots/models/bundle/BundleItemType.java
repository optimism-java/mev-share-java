package net.flashbots.models.bundle;

import java.util.Objects;

/**
 * The type BundleItemType.
 *
 * @author kaichen
 * @since 0.1.0
 */
public abstract class BundleItemType {

    /**
     * The type HashItem.
     */
    public static class HashItem extends BundleItemType {
        private String hash;

        /**
         * Instantiates a new Hash item.
         */
        public HashItem() {
            super();
        }

        /**
         * Gets hash.
         *
         * @return the hash
         */
        public String getHash() {
            return hash;
        }

        /**
         * Sets hash.
         *
         * @param hash the hash
         * @return the hash
         */
        public HashItem setHash(String hash) {
            this.hash = hash;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof HashItem hashItem)) return false;
            return Objects.equals(hash, hashItem.hash);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hash);
        }

        @Override
        public String toString() {
            return "HashItem{" + "hash='" + hash + '\'' + '}';
        }
    }

    /**
     * The type HashItem.
     */
    public static class TxItem extends BundleItemType {

        private String tx;

        private boolean canRevert;

        /**
         * Instantiates a new Tx item.
         */
        public TxItem() {
            super();
        }

        /**
         * Gets tx.
         *
         * @return the tx
         */
        public String getTx() {
            return tx;
        }

        /**
         * Sets tx.
         *
         * @param tx the tx
         * @return the tx
         */
        public TxItem setTx(String tx) {
            this.tx = tx;
            return this;
        }

        /**
         * Is can revert boolean.
         *
         * @return the boolean
         */
        public boolean isCanRevert() {
            return canRevert;
        }

        /**
         * Sets can revert.
         *
         * @param canRevert the can revert
         * @return the can revert
         */
        public TxItem setCanRevert(boolean canRevert) {
            this.canRevert = canRevert;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TxItem txItem)) return false;
            return canRevert == txItem.canRevert && Objects.equals(tx, txItem.tx);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tx, canRevert);
        }

        @Override
        public String toString() {
            return "TxItem{" + "tx='" + tx + '\'' + ", canRevert=" + canRevert + '}';
        }
    }

    /**
     * The type Bundle item.
     */
    public static class BundleItem extends BundleItemType {
        private BundleParams bundle;

        /**
         * Instantiates a new Bundle item.
         */
        public BundleItem() {
            super();
        }

        /**
         * Gets bundle.
         *
         * @return the bundle
         */
        public BundleParams getBundle() {
            return bundle;
        }

        /**
         * Sets bundle.
         *
         * @param bundle the bundle
         * @return the bundle
         */
        public BundleItem setBundle(BundleParams bundle) {
            this.bundle = bundle;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BundleItem that)) return false;
            return Objects.equals(bundle, that.bundle);
        }

        @Override
        public int hashCode() {
            return Objects.hash(bundle);
        }

        @Override
        public String toString() {
            return "BundleItem{" + "bundle=" + bundle + '}';
        }
    }

    /**
     * Instantiates a new BundleItemType.
     */
    public BundleItemType() {}
}
