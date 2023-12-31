# Contribute to mev-share-java

## Create a fork

First, create a fork of the mev-share-java repo in your own account so that you can work with your own copy.

**To create a fork using the website**

1. Log in to your Github account.
1. Browse to the [mev-share-java repo](https://github.com/optimism-java/mev-share-java) on GitHub.
1. Choose **Fork** in the top-right, then choose **Create new fork**.
1. For **Owner**, select your username.
1. For **Repository name**, we suggest keeping the name mev-share-java, but you can use any name.
1. Optional. To contribute you need only the main branch of the repo. To include all branches, unselect the checkbox for **Copy the `main` branch only**.
1. Click **Create fork**.

### Clone your fork

Next, clone your fork of the repo to your local workspace.

**To clone your fork to your local workspace**
1. Open the GitHub page for your fork of the repo, then click **Sync fork**.
1. Click **Code**, then click **HTTPS** and copy the web URL displayed.
1. Open a terminal session and navigate to the folder to use, then run the following command, replacing the URL with the URL you copied from the Git page:

`git clone https://github.com/github-user-name/mev-share-java.git`

The repo is automatically cloned into the `mev-share-java` folder in your workspace.
Create a branch of your fork with following command (or follow the [GitHub topic on branching](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-and-deleting-branches-within-your-repository))

`git checkout -b your-branch-name`

Use the following command to set the [remote upstream repo](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/working-with-forks/configuring-a-remote-repository-for-a-fork):

`git remote add upstream https://github.com/optimism-java/mev-share-java.git`

You now have a fork of the mev-share-java repo set up in your local workspace. You can make changes to the files in the workspace, add commits, then push your changes to your fork of the repo to then create a Pull Request.

`GOERLI_RPC_URL=<GOERLI_RPC_URL> SIGNER_PRIVATE_KEY=<SIGNER_PRIVATE_KEY> ./gradlew clean build`

Run build and test before you submit a Pull Request.
