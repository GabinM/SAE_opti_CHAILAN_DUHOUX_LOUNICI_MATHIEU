public interface ClusteringAlgorithm {
    /**
     * @param data  Matrice des données (Nobjet x Ncarac)
     * @param nClusters  Nombre de clusters à détecter (biomes)
     * @return Tableau de labels (indice du cluster pour chaque objet)
     */
    int[] cluster(double[][] data, int nClusters);
}
